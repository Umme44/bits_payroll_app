import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoomRequisition } from '../room-requisition.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-room-requisition-detail',
  templateUrl: './room-requisition-detail.component.html',
})
export class RoomRequisitionDetailComponent implements OnInit {
  roomRequisition: IRoomRequisition | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roomRequisition }) => {
      this.roomRequisition = roomRequisition;
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
