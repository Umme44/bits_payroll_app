import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { UserWorkFromHomeApplicationService } from '../service/user-work-from-home-application.service';
import {
  SWAL_CANCEL_BTN_TEXT,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DELETE_CONFIRMATION,
} from '../../../../shared/swal-common/swal.properties.constant';
import { swalOnDeleteSuccess, swalOnRequestError } from '../../../../shared/swal-common/swal-common';
import { UserWorkFromApplicationDetailModalComponent } from '../modal/user-work-from-application-detail-modal.component';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../../../config/color.code.constant';
import { IUserWorkFromHomeApplication } from '../user-work-from-home-application.model';

@Component({
  selector: 'jhi-user-work-from-home-application',
  templateUrl: './user-work-from-home-application.component.html',
})
export class UserWorkFromHomeApplicationComponent implements OnInit, OnDestroy {
  workFromHomeApplications: IUserWorkFromHomeApplication[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  durationDays!: number;

  constructor(protected userWorkFromHomeApplicationService: UserWorkFromHomeApplicationService, protected modalService: NgbModal) {
    this.workFromHomeApplications = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'startDate';
    this.ascending = false;
  }

  loadAll(): void {
    this.userWorkFromHomeApplicationService
      .queryFindByEmployeeId({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IUserWorkFromHomeApplication[]>) => this.paginateWorkFromHomeApplications(res.body));
  }

  reset(): void {
    this.page = 0;
    this.workFromHomeApplications = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInWorkFromHomeApplications();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      // this.eventManager.destroy(this.eventSubscriber);
      sessionStorage.removeItem('movementEntryApplyDate');
    }
  }

  trackId(index: number, item: IUserWorkFromHomeApplication): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInWorkFromHomeApplications(): void {
    // this.eventSubscriber = this.eventManager.subscribe('workFromHomeApplicationListModification', () => this.reset());
  }

  delete(workFromHomeApplication: IUserWorkFromHomeApplication): void {
    Swal.fire({
      text: SWAL_DELETE_CONFIRMATION,
      showCancelButton: true,
      confirmButtonColor: PRIMARY_COLOR,
      cancelButtonColor: DANGER_COLOR,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      cancelButtonText: SWAL_CANCEL_BTN_TEXT,
    }).then(result => {
      if (result.isConfirmed) {
        this.userWorkFromHomeApplicationService.delete(workFromHomeApplication.id!).subscribe(
          res => {
            swalOnDeleteSuccess();
            setTimeout(() => {
              this.reset();
            }, 1000);
          },
          () => {
            swalOnRequestError();
          }
        );
      }
    });
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'startDate') {
      result.push('startDate');
    }
    return result;
  }

  protected paginateWorkFromHomeApplications(data: IUserWorkFromHomeApplication[] | null): void {
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.workFromHomeApplications.push(data[i]);
      }
    }
  }

  textSlicing(note: any): string {
    return note.slice(0, 80) + '...';
  }

  openWorkFromApplicationDetails(workFromHomeApplication: IUserWorkFromHomeApplication): void {
    const modalRef = this.modalService.open(UserWorkFromApplicationDetailModalComponent, { size: 'lg', backdrop: true, keyboard: false });
    modalRef.componentInstance.workFromHomeApplication = workFromHomeApplication;
  }
}
