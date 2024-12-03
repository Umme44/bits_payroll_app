import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRecruitmentRequisitionBudget } from '../recruitment-requisition-budget.model';

@Component({
  selector: 'jhi-recruitment-requisition-budget-detail',
  templateUrl: './recruitment-requisition-budget-detail.component.html',
})
export class RecruitmentRequisitionBudgetDetailComponent implements OnInit {
  recruitmentRequisitionBudget: IRecruitmentRequisitionBudget | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recruitmentRequisitionBudget }) => {
      this.recruitmentRequisitionBudget = recruitmentRequisitionBudget;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
