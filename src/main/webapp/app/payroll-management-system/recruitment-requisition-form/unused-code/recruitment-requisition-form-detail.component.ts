import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRecruitmentRequisitionForm } from 'app/shared/model/recruitment-requisition-form.model';

@Component({
  selector: 'jhi-recruitment-requisition-form-detail',
  templateUrl: './recruitment-requisition-form-detail.component.html',
})
export class RecruitmentRequisitionFormDetailComponent implements OnInit {
  recruitmentRequisitionForm: IRecruitmentRequisitionForm | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recruitmentRequisitionForm }) => (this.recruitmentRequisitionForm = recruitmentRequisitionForm));
  }

  previousState(): void {
    window.history.back();
  }
}
