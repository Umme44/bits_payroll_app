import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProcReqMaster } from '../proc-req-master.model';
import { DataUtils } from '../../../core/util/data-util.service';
import { ProcReqMasterService } from '../service/proc-req-master.service';
import { Timeline } from '../../../shared/legacy/legacy-model/timeline-model';
import { RequisitionStatus } from '../../enumerations/requisition-status.model';

@Component({
  selector: 'jhi-proc-req-master-detail',
  templateUrl: './proc-req-master-detail.component.html',
})
export class ProcReqMasterDetailComponent implements OnInit {
  procReqMaster: IProcReqMaster | null = null;
  timelineList: Timeline[] = [];

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute, private procReqUserService: ProcReqMasterService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ procReqMaster }) => (this.procReqMaster = procReqMaster));

    this.timelineList = [{ timelineName: 'Request Initiated', effectiveDate: this.procReqMaster?.requestedDate! }];
    if (this.procReqMaster?.recommendationAt01) {
      this.timelineList.push({
        timelineName: `Recommended by ${this.procReqMaster?.recommendedBy01FullName}`,
        effectiveDate: this.procReqMaster?.recommendationAt01,
      });
    }
    if (this.procReqMaster?.recommendationAt02) {
      this.timelineList.push({
        timelineName: `Recommended by ${this.procReqMaster.recommendedBy02FullName}`,
        effectiveDate: this.procReqMaster.recommendationAt02,
      });
    }
    if (this.procReqMaster?.recommendationAt03) {
      this.timelineList.push({
        timelineName: `Recommended by ${this.procReqMaster?.recommendedBy03FullName}`,
        effectiveDate: this.procReqMaster?.recommendationAt03,
      });
    }
    if (this.procReqMaster?.recommendationAt04) {
      this.timelineList.push({
        timelineName: `Recommended by ${this.procReqMaster?.recommendedBy04FullName}`,
        effectiveDate: this.procReqMaster?.recommendationAt04,
      });
    }
    if (this.procReqMaster?.recommendationAt05) {
      this.timelineList.push({
        timelineName: `Recommended by ${this.procReqMaster?.recommendedBy05FullName}`,
        effectiveDate: this.procReqMaster?.recommendationAt05,
        note: '(Final Approval)',
      });
    }
    if (this.procReqMaster?.requisitionStatus === RequisitionStatus.CLOSED) {
      this.timelineList.push({ timelineName: `Closed`, effectiveDate: this.procReqMaster?.updatedAt! });
    }
    if (this.procReqMaster?.requisitionStatus === RequisitionStatus.NOT_APPROVED) {
      this.timelineList.push({
        timelineName: `Not approved`,
        effectiveDate: this.procReqMaster?.rejectedDate!,
        note: `(${this.procReqMaster?.rejectionReason})`,
      });
    }
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }

  downloadFile(id: number): void {
    this.procReqUserService.downloadFile(id);
  }
}

/*
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProcReqMaster } from '../proc-req-master.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-proc-req-master-detail',
  templateUrl: './proc-req-master-detail.component.html',
})
export class ProcReqMasterDetailComponent implements OnInit {
  procReqMaster: IProcReqMaster | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ procReqMaster }) => {
      this.procReqMaster = procReqMaster;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
*/
