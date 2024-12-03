import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2';
import { IPfNominee, PfNominee } from '../../pf-nominee.model';
import { IPfNomineeEmployeeDetailsDTO } from '../../../../shared/model/pf-nominee-employee-details.model';
import { PfNomineeService } from '../../service/pf-nominee.service';
import { PfAccountService } from '../../../pf-account/service/pf-account.service';
import { swalOnDeleteConfirmation } from '../../../../shared/swal-common/swal-common';

@Component({
  selector: 'jhi-pf-nominee-admin-print-details',
  templateUrl: './pf-nominee-report-admin.component.html',
  styleUrls: ['./pf-nominee-report-admin.component.scss'],
})
export class PfNomineeReportAdminComponent implements OnInit, OnDestroy {
  pfNominees!: IPfNominee[];
  employeeDetails!: IPfNomineeEmployeeDetailsDTO;
  pfNominee: IPfNominee | null = null;
  defaultEmptyNominee!: IPfNominee;
  headerName!: string;
  routeName!: string;
  guardianInfo: IPfNominee;

  editForm = this.fb.group({
    pfAccountId: [],
  });
  totalUsedPercentage = 0;
  employeePin!: string;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected pfNomineeService: PfNomineeService,
    protected pfAccountService: PfAccountService,
    private fb: FormBuilder
  ) {
    this.employeePin = activatedRoute.snapshot.params.pin;

    this.defaultEmptyNominee = {
      ...new PfNominee(),
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

  ngOnDestroy(): void {
    sessionStorage.removeItem('midHeaderOfNomineeReport');
    sessionStorage.removeItem('midRouteOfNomineeReport');
  }

  ngOnInit(): void {
    this.showPfNomineeStatement();

    if (sessionStorage.getItem('midHeaderOfNomineeReport') !== null && sessionStorage.getItem('midHeaderOfNomineeReport') !== undefined) {
      this.headerName = sessionStorage.getItem('midHeaderOfNomineeReport')!;
    }

    if (sessionStorage.getItem('midRouteOfNomineeReport') !== null && sessionStorage.getItem('midRouteOfNomineeReport') !== undefined) {
      this.routeName = sessionStorage.getItem('midRouteOfNomineeReport')!;
    }
  }

  loadEmployeeDetails(): void {
    this.pfNomineeService.getCurrentEmployeeDetails(this.employeePin).subscribe(res => (this.employeeDetails = res.body!));
  }

  showPfNomineeStatement(): void {
    this.pfNomineeService.queryForPfList(this.employeePin).subscribe(res => {
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
            ...new PfNominee(),
            guardianName: value.guardianName,
            guardianFatherOrSpouseName: value.guardianFatherOrSpouseName,
            guardianDateOfBirth: value.guardianDateOfBirth,
            guardianPresentAddress: value.guardianPresentAddress,
            guardianPermanentAddress: value.guardianPermanentAddress,
            guardianIdentityType: value.guardianIdentityType,
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
        this.pfNomineeService.delete(pfNominee.id).subscribe(
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

  getPfNomineeImage(pfNomineeId: number): String {
    const url = SERVER_API_URL + 'files/nominee-image/' + pfNomineeId;
    return url;
  }
}
