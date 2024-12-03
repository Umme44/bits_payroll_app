import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {ISpecialShiftTiming} from "../special-shift-timing.model";
import {DataUtils} from "../../../core/util/data-util.service";

@Component({
  selector: 'jhi-special-shift-timing-detail-modal',
  templateUrl: './special-shift-timing-detail-modal.component.html',
})
export class SpecialShiftTimingDetailModalComponent implements OnInit {
  specialShiftTiming: ISpecialShiftTiming | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute, public activeModal: NgbActiveModal) {}

  ngOnInit(): void {
    //this.activatedRoute.data.subscribe(({ flexScheduleApplication }) => (this.flexScheduleApplication = flexScheduleApplication));
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

  cancel(): void {
    this.activeModal.dismiss();
  }
}
