import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import Swal from 'sweetalert2';
import { OrganizationFilesUrl } from 'app/shared/constants/organization-files-url';
import { INominee, Nominee } from '../../../entities/nominee-admin/nominee.model';
import { IPfNomineeEmployeeDetailsDTO } from '../../../shared/model/pf-nominee-employee-details.model';
import { IPfNominee } from '../../../entities/nominee-admin/pf-nominee.model';
import { NomineeType } from '../../../shared/model/enumerations/nominee-type.model';
import { NomineeService } from '../../../entities/nominee-admin/service/nominee.service';
import { PfNomineeFormService } from '../../../entities/pf-nominee/update/pf-nominee-form.service';
import { swalOnDeleteSuccess, swalOnRequestError } from '../../../shared/swal-common/swal-common';

@Component({
  selector: 'jhi-gf-nominee-report',
  templateUrl: './gf-nominee-report.component.html',
  styleUrls: ['./gf-nominee-report.component.scss'],
})
export class GfNomineeReportComponent implements OnInit {
  gfNominees!: INominee[];
  nominee!: INominee;
  employeeDetails!: IPfNomineeEmployeeDetailsDTO;
  pfAccountIdFromRoute!: number;
  defaultEmptyNominee!: IPfNominee;
  totalUsedPercentage = 0;
  nomineeType!: NomineeType;

  guardianInfo!: INominee;

  editForm = this.fb.group({
    pfAccountId: [],
  });

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    private fb: FormBuilder,
    private nomineeService: NomineeService,
    private pfNomineeFormService: PfNomineeFormService,
    private nomineeFormService: NomineeService
  ) {
    const nomineeTypeFromRoute = this.activatedRoute.snapshot.data['nomineeType'];

    nomineeTypeFromRoute
      ? (this.nomineeType = nomineeTypeFromRoute === 'GENERAL' ? NomineeType.GENERAL : NomineeType.GRATUITY_FUND)
      : this.router.navigate(['/dashboard']);

    this.nominee = {
      ...new Nominee(),
      nomineeType: this.nomineeType,
    };
  }

  ngOnInit(): void {
    sessionStorage.setItem('selectedNomineeType', 'gf');
    this.loadEmployeeDetails();
  }

  loadGuardianInfo(gfNominee: INominee[]): void {
    this.guardianInfo = gfNominee.find(nominee => nominee.age! < 18)!;
    if (this.guardianInfo === undefined) {
      this.guardianInfo = { ...new Nominee() };
    }
  }

  loadGfNominees(nominee: INominee): void {
    nominee.nomineeType = this.nomineeType;
    this.nomineeService.getNomineeListCommon(nominee).subscribe(res => {
      this.gfNominees = res.body!;
      this.loadGuardianInfo(this.gfNominees);
    });
  }

  loadEmployeeDetails(): void {
    this.nomineeService.getEmployeeDetailsForNomineeCommon().subscribe(res => {
      this.employeeDetails = res.body!;
      this.loadGfNominees(this.nominee);
    });
  }

  // showGfNomineeStatement(): void {
  //   this.pfNomineeService.getAllByPfAccountId(this.pfAccountIdFromRoute).subscribe(res => {
  //     this.pfNominees = res.body!;
  //     if (this.pfNominees.length === 0) {
  //       this.pfNominees.push(this.defaultEmptyNominee);
  //       this.pfNominees.push(this.defaultEmptyNominee);
  //     } else if (this.pfNominees.length === 1) {
  //       this.loadRemainingSharePercentage(this.pfNominees[0].pfAccountId!);
  //       this.pfNominees.push(this.defaultEmptyNominee);
  //     } else if (this.pfNominees.length > 1) {
  //       this.loadRemainingSharePercentage(this.pfNominees[0].pfAccountId!);
  //     }
  //     this.loadEmployeeDetails(this.pfNominees[0].pin!);
  //     this.loadGuardianInfo();
  //   });
  // }

  // loadGuardianInfo(): void {
  //   if (this.pfNominees.length > 0) {
  //     for (let i = 0; i < this.pfNominees.length; i++) {
  //       const value = this.pfNominees[i];
  //       if (value.age! < 18) {
  //         this.guardianInfo = {
  //           guardianName: value.guardianName,
  //           guardianFatherOrSpouseName: value.guardianFatherOrSpouseName,
  //           guardianDateOfBirth: value.guardianDateOfBirth,
  //           guardianPresentAddress: value.guardianPresentAddress,
  //           guardianPermanentAddress: value.guardianPermanentAddress,
  //           guardianProofOfIdentityOfLegalGuardian: value.guardianProofOfIdentityOfLegalGuardian,
  //           guardianRelationshipWithNominee: value.guardianRelationshipWithNominee,
  //           guardianNidNumber: value.guardianNidNumber,
  //           guardianBrnNumber: value.guardianBrnNumber,
  //           guardianIdNumber: value.guardianIdNumber,
  //         };
  //         break;
  //       }
  //     }
  //   }
  // }

  print(): void {
    window.print();
  }

  delete(pfNominee: any): void {
    Swal.fire({
      text: 'Delete ?',
      showDenyButton: true,
      confirmButtonText: 'Yes',
      confirmButtonColor: '#55738f',
      denyButtonText: 'Cancel',
      denyButtonColor: '#f25d5a',
    }).then(result => {
      if (result.isConfirmed) {
        this.nomineeService.delete(pfNominee.id).subscribe(
          () => {
            this.deleteSuccess();
            // this.showGfNomineeStatement();
          },
          () => this.requestFailed()
        );
      }
    });
  }

  deleteSuccess(): void {
    swalOnDeleteSuccess();
    // this.showGfNomineeStatement();
  }

  requestFailed(): void {
    swalOnRequestError();
  }

  getGfNomineeImage(id: number): String {
    const url = SERVER_API_URL + 'files/common/nominee-image/' + id;
    return url;
  }

  getNomineeLetterHead(): string {
    return OrganizationFilesUrl.NOMINEE_LETTER_HEAD;
  }
}
