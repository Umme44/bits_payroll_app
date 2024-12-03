import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2';
import { swalOnDeleteConfirmation } from '../../../shared/swal-common/swal-common';
import { IdentityType } from '../../../shared/model/enumerations/identity-type.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { PfAccountService } from '../../../shared/legacy/legacy-service/pf-account.service';
import { IPfNominee, PfNominee } from '../../../shared/legacy/legacy-model/pf-nominee.model';
import { OrganizationFilesUrl } from '../../../shared/constants/organization-files-url';
import { IPfNomineeEmployeeDetailsDTO } from '../../../shared/model/pf-nominee-employee-details.model';
import { PfNomineeFormService } from '../../service/pf-nominee-form.service';

@Component({
  selector: 'jhi-pf-nominee-form-print-details',
  templateUrl: './pf-nominee-form-details-print-format.html',
  styleUrls: ['./pf-nominee-details-print-format.component.scss'],
})
export class PfNomineeFormPrintDetailsComponent implements OnInit {
  pfNominees!: IPfNominee[];
  employeeDetails!: IPfNomineeEmployeeDetailsDTO;
  pfNominee: IPfNominee | null = null;
  defaultEmptyNominee!: IPfNominee;

  guardianInfo: IPfNominee;

  editForm = this.fb.group({
    pfAccountId: [],
  });
  totalUsedPercentage = 0;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected pfNomineeFormService: PfNomineeFormService,
    protected pfAccountService: PfAccountService,
    private fb: FormBuilder,
    protected applicationConfigService: ApplicationConfigService
  ) {
    this.defaultEmptyNominee = {
      fullName: '',
      age: 0,
      sharePercentage: 0.0,
    };
    this.guardianInfo = {
      ...new PfNominee(),
      guardianName: '',
      guardianFatherOrSpouseName: '',
      guardianDateOfBirth: undefined,
      guardianPresentAddress: '',
      guardianPermanentAddress: '',
      guardianProofOfIdentityOfLegalGuardian: '',
      guardianRelationshipWithNominee: '',
      guardianIdNumber: '',
    };
  }

  ngOnInit(): void {
    sessionStorage.setItem('selectedNomineeType', 'pf');
    this.showPfNomineeStatement();
  }

  loadEmployeeDetails(): void {
    this.pfNomineeFormService.getCurrentEmployeeDetails().subscribe(res => (this.employeeDetails = res.body!));
  }

  showPfNomineeStatement(): void {
    this.pfNomineeFormService.queryForList().subscribe(res => {
      this.pfNominees = res.body!;
      this.loadEmployeeDetails();
      this.loadGuardianInfo();
    });
  }

  loadGuardianInfo(): void {
    if (this.pfNominees.length > 0) {
      for (let i = 0; i < this.pfNominees.length; i++) {
        const value = this.pfNominees[i];
        if (value.age! < 18) {
          this.guardianInfo = {
            guardianName: value.guardianName,
            guardianFatherOrSpouseName: value.guardianFatherOrSpouseName,
            guardianDateOfBirth: value.guardianDateOfBirth,
            guardianPresentAddress: value.guardianPresentAddress,
            guardianPermanentAddress: value.guardianPermanentAddress,
            guardianDocumentName:
              value.guardianIdentityType === IdentityType.OTHER ? value.guardianDocumentName : value.guardianIdentityType,
            guardianIdNumber: value.guardianIdNumber,
            guardianProofOfIdentityOfLegalGuardian: value.guardianProofOfIdentityOfLegalGuardian,
            guardianRelationshipWithNominee: value.guardianRelationshipWithNominee,
          };
          break;
        }
      }
    }
  }

  print(): void {
    window.print();
  }

  delete(pfNominee: any): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.pfNomineeFormService.delete(pfNominee.id).subscribe(
          () => {
            this.deleteSuccess();
          },
          () => this.requestFailed()
        );
      }
    });
  }

  deleteSuccess(): void {
    Swal.fire({
      icon: 'warning',
      text: 'Deleted',
      timer: 1500,
      showConfirmButton: false,
    });
    this.showPfNomineeStatement();
  }

  requestFailed(): void {
    Swal.fire({
      icon: 'error',
      text: 'Something went Wrong',
      timer: 1500,
      showConfirmButton: false,
    });
  }

  // todo: should be in service layer
  getPfNomineeImage(pfNomineeId: number): String {
    const url = this.applicationConfigService.getEndpointFor('files/nominee-image/' + pfNomineeId);
    return url;
  }

  getNomineeLetterHead(): string {
    return OrganizationFilesUrl.NOMINEE_LETTER_HEAD;
  }
}
