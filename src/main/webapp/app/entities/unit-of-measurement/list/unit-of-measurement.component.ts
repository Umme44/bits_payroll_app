import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { IUnitOfMeasurement } from '../unit-of-measurement.model';
import { UnitOfMeasurementService } from '../service/unit-of-measurement.service';
import { EventManager } from '../../../core/util/event-manager.service';
import { ParseLinks } from '../../../core/util/parse-links.service';
import {
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnRequestErrorWithBackEndErrorTitle,
} from '../../../shared/swal-common/swal-common';
import { UnitOfMeasurementDetailComponent } from '../detail/unit-of-measurement-detail.component';

@Component({
  selector: 'jhi-unit-of-measurement',
  templateUrl: './unit-of-measurement.component.html',
})
export class UnitOfMeasurementComponent implements OnInit, OnDestroy {
  unitOfMeasurements: IUnitOfMeasurement[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected unitOfMeasurementService: UnitOfMeasurementService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.unitOfMeasurements = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.unitOfMeasurementService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IUnitOfMeasurement[]>) => this.paginateUnitOfMeasurements(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.unitOfMeasurements = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInUnitOfMeasurements();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IUnitOfMeasurement): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInUnitOfMeasurements(): void {
    this.eventSubscriber = this.eventManager.subscribe('unitOfMeasurementListModification', () => this.reset());
  }

  delete(unitOfMeasurement: IUnitOfMeasurement): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.unitOfMeasurementService.delete(unitOfMeasurement.id!).subscribe(
          _ => {
            swalOnDeleteSuccess();
            this.reset();
          },
          _ => swalOnRequestErrorWithBackEndErrorTitle('Failed to delete! It has already used in item(s).')
        );
      }
    });
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateUnitOfMeasurements(data: IUnitOfMeasurement[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.unitOfMeasurements.push(data[i]);
      }
    }
  }

  openView(unitOfMeasurement: IUnitOfMeasurement): void {
    const modalRef = this.modalService.open(UnitOfMeasurementDetailComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.unitOfMeasurement = unitOfMeasurement;
  }
}
