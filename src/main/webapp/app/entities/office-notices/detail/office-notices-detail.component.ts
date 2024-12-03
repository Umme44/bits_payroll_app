import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOfficeNotices } from '../office-notices.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-office-notices-detail',
  templateUrl: './office-notices-detail.component.html',
})
export class OfficeNoticesDetailComponent implements OnInit {
  officeNotices: IOfficeNotices | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ officeNotices }) => {
      this.officeNotices = officeNotices;
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
