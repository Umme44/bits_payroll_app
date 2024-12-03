import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRecruitmentRequisitionForm } from '../recruitment-requisition-form.model';
import { OrganizationFilesUrl } from '../../../config/organization-files-url';

@Component({
  selector: 'jhi-recruitment-requisition-form-detail',
  templateUrl: './recruitment-requisition-form-detail.component.html',
  styleUrls: ['./recruitment-requisition-form-detail.component.scss'],
})
export class RecruitmentRequisitionFormDetailComponent implements OnInit, OnDestroy {
  recruitmentRequisitionForm: IRecruitmentRequisitionForm | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recruitmentRequisitionForm }) => (this.recruitmentRequisitionForm = recruitmentRequisitionForm));
  }

  ngOnDestroy(): void {
    sessionStorage.removeItem('rrfPrintViewMidHeader');
  }

  previousState(): void {
    window.history.back();
  }

  public downloadAsPDF(): void {
    window.print();
  }

  public getMidHeaderRoute(): string {
    if (sessionStorage.getItem('rrfPrintViewMidHeader')) return sessionStorage.getItem('rrfPrintViewMidHeader')!;
    else return '';
  }

  getRecruitmentRequisitionLetterHead(): string {
    return OrganizationFilesUrl.RECRUITMENT_REQUISITION_LETTER_HEAD;
  }

  getOrganizationStamp(): string {
    return OrganizationFilesUrl.ORGANIZATION_STAMP;
  }
}
