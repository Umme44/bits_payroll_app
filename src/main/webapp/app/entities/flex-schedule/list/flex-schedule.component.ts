import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import {FlexSchedule, IFlexSchedule} from "../flex-schedule.model";
import {FlexScheduleService} from "../service/flex-schedule.service";
import {EventManager} from "../../../core/util/event-manager.service";
import {ParseLinks} from "../../../core/util/parse-links.service";
import dayjs from 'dayjs/esm';
import {
  swalClose,
  swalOnLoading,
  swalOnRequestError,
  swalOnRequestErrorWithBackEndErrorTitle
} from "../../../shared/swal-common/swal-common";


@Component({
  selector: 'jhi-flex-schedule',
  templateUrl: './flex-schedule.component.html',
})
export class FlexScheduleComponent implements OnInit, OnDestroy {
  flexSchedules: IFlexSchedule[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  today!: any;
  monthFromToday!: any;
  isInvalid = false;
  selectedMemberId!: number;

  editForm = this.fb.group({
    startDate: [],
    endDate: [],
    employeeId: this.fb.group({
      employeeId: [null, Validators.required],
    }),
  });

  constructor(
    protected flexScheduleService: FlexScheduleService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    private fb: FormBuilder
  ) {
    this.flexSchedules = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
    this.today = new Date();
    this.monthFromToday = new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000);
  }

  get employeeIdForm(): FormGroup {
    return this.editForm.get('employeeId') as FormGroup;
  }

  loadAll(): void {
    this.flexScheduleService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IFlexSchedule[]>) => this.paginateFlexSchedules(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.flexSchedules = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFlexSchedules();
    //this.setInitialDateInDatePicker();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFlexSchedule): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFlexSchedules(): void {
    this.eventSubscriber = this.eventManager.subscribe('flexScheduleListModification', () => this.reset());
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateFlexSchedules(data: IFlexSchedule[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.flexSchedules.push(data[i]);
      }
    }
  }

  checkDate(): void {
    const startDate = this.editForm.get(['startDate'])!.value;
    const endDate = this.editForm.get(['endDate'])!.value;

    if (startDate && endDate && startDate > endDate) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
    }
  }

  setInitialDateInDatePicker(): void {
    this.editForm.controls['endDate'].setValue(dayjs(this.today));
    this.editForm.controls['startDate'].setValue(dayjs(this.monthFromToday));
  }

  loadFromRangeAndFlexSchedule(): void {
    const timeRange = this.createFromForm();
    this.flexScheduleService
      .getFlexScheduleByEffectiveDates(timeRange, { page: this.page, size: this.itemsPerPage, sort: this.sort() })
      .subscribe((res: HttpResponse<IFlexSchedule[]>) => {
        this.flexSchedules = res.body || [];
      });
  }
  private createFromForm(): IFlexSchedule {
    return {
      ...new FlexSchedule(),
      employeeId: this.employeeIdForm.get(['employeeId'])!.value,
      endDate: this.editForm.get(['endDate'])!.value ? this.editForm.get(['endDate'])!.value.format('YYYY-MM-DD') : undefined,
      startDate: this.editForm.get(['startDate'])!.value ? this.editForm.get(['startDate'])!.value.format('YYYY-MM-DD') : undefined,
    };
  }

  loadLastThirtyDays(): void {
    this.setInitialDateInDatePicker();
    const timeRange = this.createFromForm();
    this.flexScheduleService
      .getFlexScheduleByEffectiveDates(timeRange, { page: this.page, size: this.itemsPerPage, sort: this.sort() })
      .subscribe((res: HttpResponse<IFlexSchedule[]>) => {
        this.flexSchedules = res.body || [];
      });
  }

  clearAll(): void {
    this.editForm.reset();
    this.reset();
    //this.setInitialDateInDatePicker();
  }

  exportAsFlexScheduleData(): void {
    const fileName = 'flex-schedule.xlsx';
    const timeRange = this.createFromForm();
    swalOnLoading('Loading ...');
    this.flexScheduleService.exportAsFlexScheduleDataInXL().subscribe(
      x => {
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        const newBlob = new Blob([x], { type: 'application/octet-stream' });

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
        swalClose();
      },
      err => {
        swalClose();
        swalOnRequestError();
      }
    );
  }

  exportAsMissingFlexScheduleData(): void {
    const fileName = 'missing-flex-schedule.xlsx';
    const timeRange = this.createFromForm();
    swalOnLoading('Loading ...');
    this.flexScheduleService.exportAsMissingFlexScheduleDataInXL().subscribe(
      x => {
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        const newBlob = new Blob([x], { type: 'application/octet-stream' });

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
        swalClose();
      },
      err => {
        swalOnRequestErrorWithBackEndErrorTitle(err.error.title);
      }
    );
  }
}
