import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { InsuranceRelation } from '../../shared/model/enumerations/insurance-relation.model';
import { IInsuranceRegistration } from '../../shared/legacy/legacy-model/insurance-registration.model';

@Component({
  selector: 'jhi-user-insurance-registration-detail',
  templateUrl: './user-insurance-registration-details.component.html',
})
export class UserInsuranceRegistrationDetailsComponent implements OnInit {
  insuranceRegistration!: IInsuranceRegistration;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ insuranceRegistration }) => (this.insuranceRegistration = insuranceRegistration));
  }

  // byteSize(base64String: string): string {
  //   return this.dataUtils.byteSize(base64String);
  // }
  //
  // openFile(contentType = '', base64String: string): void {
  //   this.dataUtils.openFile(contentType, base64String);
  // }

  previousState(): void {
    window.history.back();
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
}
