import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IItemInformation } from '../item-information.model';
import { DataUtils } from '../../../core/util/data-util.service';

@Component({
  selector: 'jhi-item-information-detail',
  templateUrl: './item-information-detail.component.html',
})
export class ItemInformationDetailComponent implements OnInit {
  itemInformation: IItemInformation | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemInformation }) => (this.itemInformation = itemInformation));
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
}

/*
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItemInformation } from '../item-information.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-item-information-detail',
  templateUrl: './item-information-detail.component.html',
})
export class ItemInformationDetailComponent implements OnInit {
  itemInformation: IItemInformation | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemInformation }) => {
      this.itemInformation = itemInformation;
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
