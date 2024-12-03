import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormControl, Validators } from '@angular/forms';

import { IConfig } from '../config.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ASC, DEFAULT_SORT_DATA, DESC, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { ConfigService, EntityArrayResponseType } from '../service/config.service';
import { ConfigDeleteDialogComponent } from '../delete/config-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { swalForWarningWithMessage, swalSuccessWithMessage } from '../../../shared/swal-common/swal-common';
import { DefinedKeys } from '../../../config/defined-keys.constant';

@Component({
  selector: 'jhi-config',
  templateUrl: './config.component.html',
})
export class ConfigComponent implements OnInit {
  configs?: IConfig[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  links: { [key: string]: number } = {
    last: 0,
  };
  page = 1;

  maxDaysForChangeAttendanceStatusFormControl = new FormControl(null, [Validators.min(1)]);
  maxDaysForAttendanceDataLoadFormControl = new FormControl(null, [Validators.min(30)]);

  maxAllowedDayForChangeAttendanceStatusConfig!: IConfig;
  maxDaysForAttendanceDataLoadConfig!: IConfig;

  isLeaveApplicationEnabled = false;
  leaveApplicationEnableConfig!: IConfig;
  constructor(
    protected configService: ConfigService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected parseLinks: ParseLinks,
    protected modalService: NgbModal,
    private fb: FormBuilder
  ) {}

  reset(): void {
    this.page = 1;
    this.configs = [];
    this.load();
  }

  loadPage(page: number): void {
    this.page = page;
    this.load();
  }

  trackId = (_index: number, item: IConfig): number => this.configService.getConfigIdentifier(item);

  ngOnInit(): void {
    this.load();
    this.loadIsLeaveApplicationEnabled();
    this.loadMaxAllowedDaysForChangeStatus();
    this.loadMaxDaysForAttendanceLoad();
  }

  delete(config: IConfig): void {
    const modalRef = this.modalService.open(ConfigDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.config = config;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.configs = [];
          this.onResponseSuccess(res);
        },
      });
  }

  load(): void {
    this.isLoading = true;
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
        this.isLoading = false;
      },
      error: (error) => {
        this.isLoading = false;
      }
    });
  }

  loadIsLeaveApplicationEnabled(): void {
    this.configService.findByKey(DefinedKeys.is_leave_application_enabled_for_user_end).subscribe(res => {
      this.leaveApplicationEnableConfig = res.body!;
      if (this.leaveApplicationEnableConfig.value === 'TRUE') this.isLeaveApplicationEnabled = true;
      else this.isLeaveApplicationEnabled = false;
    });
  }

  loadMaxAllowedDaysForChangeStatus(): void {
    this.configService.findByKey(DefinedKeys.max_allowed_previous_days_for_change_attendance_status).subscribe(res => {
      this.maxAllowedDayForChangeAttendanceStatusConfig = res.body!;
      this.maxDaysForChangeAttendanceStatusFormControl.setValue(this.maxAllowedDayForChangeAttendanceStatusConfig.value);
    });
  }

  loadMaxDaysForAttendanceLoad(): void {
    this.configService.findByKey(DefinedKeys.max_duration_in_days_for_attendance_data_load).subscribe(res => {
      this.maxDaysForAttendanceDataLoadConfig = res.body!;
      this.maxDaysForAttendanceDataLoadFormControl.setValue(this.maxDaysForAttendanceDataLoadConfig.value);
    });
  }
  changeFlagOfUserLeaveApplication(): void {
    if (this.isLeaveApplicationEnabled) this.leaveApplicationEnableConfig.value = 'FALSE';
    else this.leaveApplicationEnableConfig.value = 'TRUE';
    this.configService.update(this.leaveApplicationEnableConfig).subscribe(res => {
      this.leaveApplicationEnableConfig = res.body!;
      this.isLeaveApplicationEnabled = !this.isLeaveApplicationEnabled;
      if (this.isLeaveApplicationEnabled) swalSuccessWithMessage('Leave Application has enabled!');
      else swalForWarningWithMessage('Leave Application has disabled!');
    });
  }

  changeMaxAllowedDaysChangeAttendanceStatus(): void {
    this.maxAllowedDayForChangeAttendanceStatusConfig.value = this.maxDaysForChangeAttendanceStatusFormControl.value;
    this.configService.update(this.maxAllowedDayForChangeAttendanceStatusConfig).subscribe(res => {
      this.maxAllowedDayForChangeAttendanceStatusConfig = res.body!;
      swalSuccessWithMessage('Saved!');
    });
  }

  changeMaxDaysForAttendanceDataLoad(): void {
    this.maxDaysForAttendanceDataLoadConfig.value = this.maxDaysForAttendanceDataLoadFormControl.value;
    this.configService.update(this.maxDaysForAttendanceDataLoadConfig).subscribe(res => {
      this.maxDaysForAttendanceDataLoadConfig = res.body!;
      swalSuccessWithMessage('Saved!');
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.page, this.predicate, this.ascending);
  }

  navigateToPage(page = this.page): void {
    this.handleNavigation(page, this.predicate, this.ascending);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.page, this.predicate, this.ascending))
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.configs = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IConfig[] | null): IConfig[] {
    const configsNew = this.configs ?? [];
    if (data) {
      for (const d of data) {
        if (configsNew.map(op => op.id).indexOf(d.id) === -1) {
          configsNew.push(d);
        }
      }
    }
    return configsNew;
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
  }

  protected queryBackend(page?: number, predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.configService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }
}
