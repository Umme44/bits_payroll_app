import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEMS_PER_PAGE } from '../../../shared/constants/pagination.constants';
import { FlexScheduleApplicationUserService } from './flex-schedule-application-user.service';
import {
  swalForWarningWithMessage,
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnRequestError,
} from '../../../shared/swal-common/swal-common';
import { FlexScheduleApplicationDetailUserModalComponent } from './flex-schedule-application-detail-user-modal.component';
import { IFlexScheduleApplication } from '../../../shared/legacy/legacy-model/flex-schedule-application.model';

@Component({
  selector: 'jhi-flex-schedule-application',
  templateUrl: './flex-schedule-application-user.component.html',
})
export class FlexScheduleApplicationUserComponent implements OnInit {
  flexScheduleApplications: IFlexScheduleApplication[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected flexScheduleApplicationUserService: FlexScheduleApplicationUserService, protected modalService: NgbModal) {
    this.flexScheduleApplications = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = false;
  }

  loadAll(): void {
    this.flexScheduleApplicationUserService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IFlexScheduleApplication[]>) => this.paginateFlexScheduleApplications(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.flexScheduleApplications = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    // this.registerChangeInFlexScheduleApplications();
  }

  // ngOnDestroy(): void {
  //   if (this.eventSubscriber) {
  //     this.eventManager.destroy(this.eventSubscriber);
  //   }
  //   this.modalService.dismissAll();
  // }

  trackId(index: number, item: IFlexScheduleApplication): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  // byteSize(base64String: string): string {
  //   return this.dataUtils.byteSize(base64String);
  // }

  // openFile(contentType = '', base64String: string): void {
  //   return this.dataUtils.openFile(contentType, base64String);
  // }

  // registerChangeInFlexScheduleApplications(): void {
  //   this.eventSubscriber = this.eventManager.subscribe('flexScheduleApplicationListModification', () => this.reset());
  // }

  delete(flexScheduleApplication: IFlexScheduleApplication): void {
    if (flexScheduleApplication.id) {
      const idToDelete = flexScheduleApplication.id;
      swalOnDeleteConfirmation().then(result => {
        if (result.isConfirmed) {
          this.flexScheduleApplicationUserService.delete(idToDelete).subscribe(
            () => {
              swalOnDeleteSuccess();
              this.reset();
            },
            () => swalOnRequestError()
          );
        }
      });
    } else {
      swalForWarningWithMessage('Id is null!');
    }
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateFlexScheduleApplications(data: IFlexScheduleApplication[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    // this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.flexScheduleApplications.push(data[i]);
      }
    }
  }

  openView(flexScheduleApplication: IFlexScheduleApplication): void {
    const modalRef = this.modalService.open(FlexScheduleApplicationDetailUserModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.flexScheduleApplication = flexScheduleApplication;
  }
}
