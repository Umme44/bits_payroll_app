import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IProcReqMaster } from '../../proc-req-master/proc-req-master.model';
import { DataUtils } from '../../../core/util/data-util.service';
import { ProcReqService } from '../service/proc-req.service';
import { RequisitionStatus } from '../../enumerations/requisition-status.model';
import { Timeline } from '../../../shared/legacy/legacy-model/timeline-model';
import { swalClose, swalOnLoading } from '../../../shared/swal-common/swal-common';

@Component({
  selector: 'jhi-proc-req-master-detail',
  templateUrl: './proc-req-detail.component.html',
  styleUrls: ['./proc-req-detail.component.scss'],
})
export class ProcReqDetailComponent implements OnInit {
  procReqMaster: IProcReqMaster | null = null;
  timelineList: Timeline[] = [];

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute, private procReqUserService: ProcReqService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ procReqMaster }) => {
      this.procReqMaster = procReqMaster;

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
    });
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

  private getFileName(disposition: string): string {
    const utf8FilenameRegex = /filename\*=UTF-8''([\w%\-\\.]+)(?:; ?|$)/i;
    const asciiFilenameRegex = /^filename=(["']?)(.*?[^\\])\1(?:; ?|$)/i;

    let fileName = '';
    if (utf8FilenameRegex.test(disposition)) {
      fileName = decodeURIComponent(utf8FilenameRegex.exec(disposition)![1]);
    } else {
      // prevent ReDos attacks by anchoring the ascii regex to string start and
      //  slicing off everything before 'filename='
      const filenameStart = disposition.toLowerCase().indexOf('filename=');
      if (filenameStart >= 0) {
        const partialDisposition = disposition.slice(filenameStart);
        const matches = asciiFilenameRegex.exec(partialDisposition);
        if (matches != null && matches[2]) {
          fileName = matches[2];
        }
      }
    }
    return fileName;
  }

  downloadFile(id: number): void {
    //this.procReqUserService.downloadFile(id);
    swalOnLoading('Preparing for download...');
    this.procReqUserService.downloadFileCommonUser(id).subscribe(
      x => {
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should

        const newBlob = new Blob([x.body!], { type: 'application/octet-stream' });
        const fileName = this.getFileName(x.headers.get('content-disposition')!);

        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob
        if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
          (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
          return;
        }

        // For other browsers:
        // Create a link pointing to the ObjectURL containing the blob.
        const data = window.URL.createObjectURL(newBlob);

        const link = document.createElement('a');
        link.href = data;
        link.download = fileName;
        // this is necessary as link.click() does not work on the latest firefox
        link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

        // tslint:disable-next-line:typedef
        setTimeout(function () {
          // For Firefox it is necessary to delay revoking the ObjectURL
          window.URL.revokeObjectURL(data);
          link.remove();
        }, 100);
      },
      err => {},
      () => {
        swalClose();
      }
    );
  }
}
