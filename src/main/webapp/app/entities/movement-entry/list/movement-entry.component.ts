import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import Swal from 'sweetalert2';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { IMovementEntry } from '../movement-entry.model';
import { MovementEntryService } from '../service/movement-entry.service';
import { EventManager } from '../../../core/util/event-manager.service';
import dayjs from 'dayjs/esm';
import {
  SWAL_CANCEL_BTN_TEXT,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DELETE_CONFIRMATION,
} from '../../../shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../../config/color.code.constant';
import { swalOnDeleteSuccess, swalOnRequestError } from '../../../shared/swal-common/swal-common';
import { MovementType } from '../../enumerations/movement-type.model';

@Component({
  selector: 'jhi-movement-entry',
  templateUrl: './movement-entry.component.html',
})
export class MovementEntryComponent implements OnInit, OnDestroy {
  movementEntries?: IMovementEntry[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  isInvalid = false;

  searchParamsForm = this.fb.group({
    startDate: new FormControl('', []),
    endDate: new FormControl('', []),
    employeeId: this.fb.group({
      employeeId: new FormControl('', []),
    }),
    leaveType: new FormControl('', []),
  });

  constructor(
    protected movementEntryService: MovementEntryService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected fb: FormBuilder
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.movementEntryService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IMovementEntry[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInMovementEntries();
  }

  protected handleNavigation(): void {
    combineLatest(this.activatedRoute.data, this.activatedRoute.queryParamMap, (data: Data, params: ParamMap) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    }).subscribe();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMovementEntry): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  get employeeIdForm(): FormGroup {
    return this.searchParamsForm.get('employeeId') as FormGroup;
  }

  loadAfterSearching(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;
    const startDate1: string = this.searchParamsForm.get('startDate')!.value
      ? dayjs(this.searchParamsForm.get('startDate')!.value).format('YYYY-MM-DD')
      : '';
    const endDate1: string = this.searchParamsForm.get('endDate')!.value
      ? dayjs(this.searchParamsForm.get('endDate')!.value).format('YYYY-MM-DD')
      : '';

    this.movementEntryService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        employeeId: this.searchParamsForm.get('employeeId')!.value.employeeId ?? '',
        startDate: startDate1,
        endDate: endDate1,
      })
      .subscribe(
        (res: HttpResponse<IMovementEntry[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  checkDate(): void {
    const startDate = this.searchParamsForm.get(['startDate'])!.value;
    const endDate = this.searchParamsForm.get(['endDate'])!.value;

    if (startDate !== undefined && endDate !== undefined && startDate > endDate) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
    }
  }

  registerChangeInMovementEntries(): void {
    this.eventSubscriber = this.eventManager.subscribe('movementEntryListModification', () => this.loadPage());
  }

  delete(movementEntry: IMovementEntry): void {
    Swal.fire({
      text: SWAL_DELETE_CONFIRMATION,
      showCancelButton: true,
      confirmButtonColor: PRIMARY_COLOR,
      cancelButtonColor: DANGER_COLOR,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      cancelButtonText: SWAL_CANCEL_BTN_TEXT,
    }).then(result => {
      if (result.isConfirmed) {
        this.movementEntryService.delete(movementEntry.id!).subscribe(
          res => {
            swalOnDeleteSuccess();
            setTimeout(() => {
              this.loadPage();
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
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IMovementEntry[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/movement-entry'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.movementEntries = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  textSlicing(startNote: any): string {
    return startNote.slice(0, 20) + '...';
  }

  movementTypeNaturalText(type: MovementType): string {
    if (type === MovementType.MOVEMENT) return 'Movement';
    else return '';
  }
}
