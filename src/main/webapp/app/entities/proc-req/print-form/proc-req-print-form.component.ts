import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IProcReqMaster } from '../../proc-req-master/proc-req-master.model';
import { OrganizationFilesUrl } from '../../../shared/constants/organization-files-url';

@Component({
  selector: 'jhi-proc-req-print-form',
  templateUrl: './proc-req-print-form.component.html',
  styleUrls: ['./proc-req-print-form.component.scss'],
})
export class ProcReqPrintFormComponent implements OnInit {
  procReqMaster: IProcReqMaster | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ procReqMaster }) => (this.procReqMaster = procReqMaster));
  }

  getMidHeaderRoute(): string {
    if (sessionStorage.getItem('prfPrintViewMidHeader')) return sessionStorage.getItem('prfPrintViewMidHeader')!;
    else return '';
  }

  downloadAsPDF(): void {
    window.print();
  }

  getRecruitmentRequisitionLetterHead(): string {
    return OrganizationFilesUrl.RECRUITMENT_REQUISITION_LETTER_HEAD;
  }
}
