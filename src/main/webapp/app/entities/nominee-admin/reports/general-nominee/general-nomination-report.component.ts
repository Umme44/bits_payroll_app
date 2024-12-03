import { Component, OnInit } from '@angular/core';
import { NomineeService } from '../../service/nominee.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { IPfNomineeEmployeeDetailsDTO } from '../../../../shared/model/pf-nominee-employee-details.model';
import { NomineeType } from '../../../../shared/model/enumerations/nominee-type.model';
import Swal from 'sweetalert2';
import { swalOnDeleteSuccess, swalOnRequestError } from '../../../../shared/swal-common/swal-common';
import { INominee, Nominee } from '../../nominee.model';
import { PfNomineeService } from '../../service/pf-nominee.service';
import { OrganizationFilesUrl } from '../../../../shared/constants/organization-files-url';
import { PfAccountService } from '../../../pf-account/service/pf-account.service';
import { NomineeFormService } from '../../update/edit-form-service/nominee-form.service';

@Component({
  selector: 'jhi-general-nomination-form',
  templateUrl: './general-nomination-report.component.html',
  styleUrls: ['./general-nomination-report.component.scss'],
})
export class GeneralNominationReportComponent implements OnInit {
  generalNominees!: INominee[];
  nominee!: INominee;
  employeeDetails!: IPfNomineeEmployeeDetailsDTO;
  nomineeType!: NomineeType;

  organizationFullName = '';

  editForm = this.nomineeFormService.createNomineeFormGroup();

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected pfNomineeService: PfNomineeService,
    protected pfAccountService: PfAccountService,
    protected router: Router,
    private fb: FormBuilder,
    private nomineeService: NomineeService,
    private nomineeFormService: NomineeFormService
  ) {
    const nomineeTypeFromRoute = this.activatedRoute.snapshot.data['nomineeType'];
    nomineeTypeFromRoute ? (this.nomineeType = nomineeTypeFromRoute) : this.router.navigate(['/dashboard']);

    this.nominee = {
      ...new Nominee(),
      nomineeType: this.nomineeType === 'GENERAL' ? NomineeType.GENERAL : NomineeType.GRATUITY_FUND,
    };

    if (sessionStorage.getItem('organizationFullName')) {
      this.organizationFullName = sessionStorage.getItem('organizationFullName')!;
    }
  }

  ngOnInit(): void {
    sessionStorage.setItem('selectedNomineeType', 'general');
    this.loadEmployeeDetails();
  }

  loadEmployeeDetails(): void {
    this.nomineeService.getEmployeeDetailsForNomineeCommon().subscribe(res => {
      this.employeeDetails = res.body!;
      this.loadGeneralNominees(this.nominee);
    });
  }

  loadGeneralNominees(nominee: INominee): void {
    nominee.nomineeType = this.nomineeType;
    this.nomineeService.getNomineeListCommon(nominee).subscribe(res => {
      this.generalNominees = res.body!;
    });
  }

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

  getGeneralNomineeImage(id: number): String {
    const url = SERVER_API_URL + 'files/common/nominee-image/' + id;
    return url;
  }

  getNomineeLetterHead(): string {
    return OrganizationFilesUrl.NOMINEE_LETTER_HEAD;
  }
}
