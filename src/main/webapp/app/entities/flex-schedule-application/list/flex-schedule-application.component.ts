import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ITEMS_PER_PAGE } from '../../../shared/constants/pagination.constants';
import dayjs from 'dayjs/esm';
import {
  swalClose,
  swalForWarningWithMessage,
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnLoading,
  swalOnRequestError,
  swalOnRequestErrorWithBackEndErrorTitle,
} from '../../../shared/swal-common/swal-common';
import {IFlexScheduleApplication} from "../flex-schedule-application.model";
import {ITimeSlot} from "../../time-slot/time-slot.model";
import {FlexScheduleApplicationService} from "../service/flex-schedule-application.service";
import {DataUtils} from "../../../core/util/data-util.service";
import {EventManager} from "../../../core/util/event-manager.service";
import {ParseLinks} from "../../../core/util/parse-links.service";
import {TimeSlotService} from "../../time-slot/service/time-slot.service";
import {DATE_FORMAT} from "../../../config/input.constants";
import {FlexScheduleApplicationDetailComponent} from "../detail/flex-schedule-application-detail.component";


@Component({
  selector: 'jhi-flex-schedule-application',
  templateUrl: './flex-schedule-application.component.html',
})
export class FlexScheduleApplicationComponent implements OnInit, OnDestroy {
  flexScheduleApplications: IFlexScheduleApplication[] = [];
  timeslots: ITimeSlot[] = [];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  startDateDp: any;
  endDateDp: any;
  isDateInvalid = false;

  editForm = this.fb.group({
    startDate: new FormControl('', []),
    endDate: new FormControl('', []),
    employeeId: this.fb.group({
      employeeId: new FormControl('', []),
    }),
    status: new FormControl('', []),
    timeSlotIdList: [],
  });

  get employeeIdForm(): FormGroup {
    return this.editForm.get('employeeId') as FormGroup;
  }

  constructor(
    protected flexScheduleApplicationService: FlexScheduleApplicationService,
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    private fb: FormBuilder,
    protected parseLinks: ParseLinks,
    protected timeSlotService: TimeSlotService
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = false;
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFlexScheduleApplications();
    this.timeSlotService.findAll().subscribe((res: HttpResponse<ITimeSlot[]>) => {
      this.timeslots = res.body || [];
      this.timeslots = this.timeslots.map(item => {
        return {
          id: item.id,
          title:
            dayjs(item.inTime, 'HH:mm:ss').format('hh:mm A') +
            ' - ' +
            dayjs(item.outTime, 'HH:mm:ss').format('hh:mm A') +
            ' - ' +
            item.title,
        };
      });
    });
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
    this.modalService.dismissAll();
  }

  loadAll(): void {
    const startRange = this.editForm.get(['startDate'])!.value;
    const endRange = this.editForm.get(['endDate'])!.value;
    const pageToLoad: number = this.page || 1;

    this.flexScheduleApplicationService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        employeeId: this.employeeIdForm.get('employeeId')!.value,
        startDate: startRange ? dayjs(startRange).format(DATE_FORMAT) : undefined,
        endDate: endRange ? dayjs(endRange).format(DATE_FORMAT) : undefined,
        status: this.editForm.get(['status'])!.value,
        timeSlotIdList: this.editForm.get('timeSlotIdList')!.value,
      })
      .subscribe((res: HttpResponse<IFlexScheduleApplication[]>) => this.paginateFlexScheduleApplications(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.flexScheduleApplications = [];
    this.loadAll();
  }

  resetEditForm(): void {
    this.editForm.reset();
    this.reset();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  getProfilePicture(pin: String): String {
    return SERVER_API_URL + '/files/get-employees-image/' + pin;
  }

  trackId(index: number, item: IFlexScheduleApplication): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInFlexScheduleApplications(): void {
    this.eventSubscriber = this.eventManager.subscribe('flexScheduleApplicationListModification', () => this.reset());
  }

  openView(flexScheduleApplication: IFlexScheduleApplication): void {
    const modalRef = this.modalService.open(FlexScheduleApplicationDetailComponent, { size: 'lg', backdrop: true });
    modalRef.componentInstance.flexScheduleApplication = flexScheduleApplication;
  }

  delete(flexScheduleApplication: IFlexScheduleApplication): void {
    if (flexScheduleApplication.id) {
      const idToDelete = flexScheduleApplication.id;
      swalOnDeleteConfirmation().then(result => {
        if (result.isConfirmed) {
          this.flexScheduleApplicationService.delete(idToDelete).subscribe(
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
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.flexScheduleApplications.push(data[i]);
      }
    }
  }

  search(): void {
    if (!this.isDateInvalid) {
      this.reset();
    }
  }

  checkDate(): void {
    const startDate = this.editForm.get('startDate')!.value;
    const endDate = this.editForm.get('endDate')!.value;
    this.isDateInvalid = false;

    if (startDate && endDate) {
      if (dayjs(startDate) > dayjs(endDate)) {
        this.isDateInvalid = true;
      } else {
        this.isDateInvalid = false;
        this.search();
      }
    } else if (!startDate && endDate) {
      this.isDateInvalid = true;
    } else if (startDate && !endDate) {
      this.isDateInvalid = true;
    } else {
      this.search();
    }
  }

  textSlice(text: string): String {
    return text.slice(0, 25) + '...';
  }

  exportAsFlexScheduleApplicationReportData(): void {
    const fileName = 'flex-schedule-applications-report.xlsx';
    const startDate = this.editForm.get(['startDate'])!.value;
    const endDate = this.editForm.get(['endDate'])!.value;
    const pageToLoad: number = this.page || 1;

    swalOnLoading('Loading ...');
    this.flexScheduleApplicationService
      .exportAsFlexScheduleReportDataInXLByDateRange({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        employeeId: this.employeeIdForm.get('employeeId')!.value,
        startDate: startDate ? dayjs(startDate).format(DATE_FORMAT) : undefined,
        endDate: endDate ? dayjs(endDate).format(DATE_FORMAT) : undefined,
        status: this.editForm.get(['status'])!.value,
        timeSlotIdList: this.editForm.get('timeSlotIdList')!.value,
      })
      .subscribe(
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
        erroObject => {
          swalOnRequestErrorWithBackEndErrorTitle(erroObject?.error?.title);
        }
      );
  }
}
