import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { InsuranceRelation } from '../../shared/model/enumerations/insurance-relation.model';
import { IInsuranceClaim } from '../../shared/legacy/legacy-model/insurance-claim.model';
import { UserInsuranceService } from './user-insurance.service';

@Component({
  selector: 'jhi-user-insurance-claim-detail',
  templateUrl: './user-insurance-claim-detail.component.html',
})
export class UserInsuranceClaimDetailComponent implements OnInit {
  insuranceClaim!: IInsuranceClaim;
  insuranceClaimId!: number;

  constructor(protected activatedRoute: ActivatedRoute, protected userService: UserInsuranceService) {}

  ngOnInit(): void {
    const id = this.activatedRoute.snapshot.params.id;
    this.insuranceClaimId = id;

    this.userService.findInsuranceClaimById(id).subscribe(res => (this.insuranceClaim = res.body!));
  }

  previousState(): void {
    window.history.back();
  }

  getInsuranceRelationName(relationName: string): string {
    if (relationName === InsuranceRelation.SELF) {
      return 'Self';
    } else if (relationName === InsuranceRelation.SPOUSE) {
      return 'Spouse';
    } else if (relationName === InsuranceRelation.CHILD_1) {
      return 'Child 1';
    } else if (relationName === InsuranceRelation.CHILD_2) {
      return 'Child 2';
    } else {
      return 'Child 3';
    }
  }
}
