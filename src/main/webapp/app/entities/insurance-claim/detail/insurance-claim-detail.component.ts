import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {IInsuranceClaim} from "../insurance-claim.model";
import {InsuranceRelation} from "../../../shared/model/enumerations/insurance-relation.model";

@Component({
  selector: 'jhi-insurance-claim-detail',
  templateUrl: './insurance-claim-detail.component.html',
})
export class InsuranceClaimDetailComponent implements OnInit {
  insuranceClaim: IInsuranceClaim | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ insuranceClaim }) => (this.insuranceClaim = insuranceClaim));
  }

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
