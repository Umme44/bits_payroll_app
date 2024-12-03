import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IRecruitmentRequisitionForm } from '../../../shared/legacy/legacy-model/recruitment-requisition-form.model';

@Component({
  selector: 'jhi-recruitment-requisition-form-details-print-view',
  templateUrl: './recruitment-requisition-form-detail-print-view.component.html',
  styleUrls: ['./recruitment-requisition-form-detail.component.scss'],
})
export class RecruitmentRequisitionFormDetailPrintViewComponent implements OnInit {
  recruitmentRequisitionForm: IRecruitmentRequisitionForm | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recruitmentRequisitionForm }) => (this.recruitmentRequisitionForm = recruitmentRequisitionForm));
  }

  previousState(): void {
    window.history.back();
  }

  public downloadAsPDF(): void {
    window.print();
  }
}
