import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnNoUnregisteredRelation,
  swalOnRequestError,
} from 'app/shared/swal-common/swal-common';
import { InsuranceRelation } from '../../shared/model/enumerations/insurance-relation.model';
import { IInsuranceConfiguration } from '../../shared/legacy/legacy-model/insurance-configuration.model';
import { InsuranceClaim } from '../../shared/legacy/legacy-model/insurance-claim.model';
import { IEmployee } from '../../shared/legacy/legacy-model/employee.model';
import { UserInsuranceService } from './user-insurance.service';
import { IInsuranceRegistration } from '../../shared/legacy/legacy-model/insurance-registration.model';

@Component({
  selector: 'jhi-insurance-profile',
  templateUrl: './insurance-profile.component.html',
  styleUrls: ['insurance-profile.component.scss'],
})
export class InsuranceProfileComponent implements OnInit {
  insuranceRegistrations!: IInsuranceRegistration[];
  insuranceConfiguration!: IInsuranceConfiguration;
  insuranceClaims!: InsuranceClaim[];

  hasAnyUnregisteredRelation = true;

  employee!: IEmployee;

  constructor(private userInsuranceService: UserInsuranceService, private router: Router) {}

  ngOnInit(): void {
    this.getInsuranceConfiguration();
    this.loadAllInsuranceClaimEntries();
    this.loadAllInsuranceRegistrations();
  }

  getInsuranceConfiguration(): void {
    this.userInsuranceService.queryInsuranceConfiguration().subscribe(res => {
      this.insuranceConfiguration = res.body!;
    });
  }

  redirectToRegisterPage(): string {
    if (this.hasAnyUnregisteredRelation) {
      return `/insurance-profile/registration`;
    } else {
      swalOnNoUnregisteredRelation();
      return '/insurance-profile';
    }
  }

  loadAllInsuranceClaimEntries(): void {
    this.userInsuranceService.queryInsuranceClaim().subscribe(res => (this.insuranceClaims = res.body!));
  }

  loadAllInsuranceRegistrations(): void {
    this.userInsuranceService.queryAllInsuranceRegistrations().subscribe(res => (this.insuranceRegistrations = res.body!));
  }

  deleteRegisteredPerson(id: number): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.userInsuranceService.deleteInsuranceRegistration(id).subscribe(
          res => {
            swalOnDeleteSuccess();
            this.loadAllInsuranceRegistrations();
          },
          () => this.requestFailed()
        );
      }
    });
  }

  requestFailed(): void {
    swalOnRequestError();
  }

  deleteClaim(id: number): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.userInsuranceService.deleteInsuranceClaim(id).subscribe(
          res => {
            swalOnDeleteSuccess();
            this.loadAllInsuranceClaimEntries();
          },
          () => this.requestFailed()
        );
      }
    });
  }

  getRegistrationAccountImageImage(registrationId: number): String {
    const url = SERVER_API_URL + 'files/common/insurance-registration-image/' + registrationId;
    return url;
  }

  trackId(index: number, item: IInsuranceRegistration): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  getInsuranceRelation(insuranceRelation: InsuranceRelation): string {
    if (insuranceRelation === InsuranceRelation.SELF) {
      return 'Self';
    } else if (insuranceRelation === InsuranceRelation.SPOUSE) {
      return 'Spouse';
    } else if (insuranceRelation === InsuranceRelation.CHILD_1) {
      return 'Child 1';
    } else if (insuranceRelation === InsuranceRelation.CHILD_2) {
      return 'Child 2';
    } else {
      return 'Child 3';
    }
  }

  redirectToClaimUrl(): void {
    (window as any).open(this.insuranceConfiguration.insuranceClaimLink!, '_blank');
  }
}
