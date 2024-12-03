import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

/* import { IFestivalBonusDetails } from 'app/shared/model/festival-bonus-details.model'; */

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
/* import { FestivalBonusDetailsService } from '../../festival-bonus-details/festival-bonus-details.service'; */
import { Filter } from '../../../common/employee-address-book/filter.model';
import {
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnHoldOrUnholdConfirmation,
  swalOnHoldOrUnholdSuccess,
  swalOnRequestError,
} from '../../../shared/swal-common/swal-common';
import {IFestivalBonusDetails} from "../../festival-bonus-details/festival-bonus-details.model";
import {FestivalBonusDetailsService} from "../../festival-bonus-details/service/festival-bonus-details.service";

@Component({
  selector: 'jhi-festival-bonus-details-list',
  templateUrl: './festival-bonus-details.component.html',
  styleUrls: ['./festival-bonus-details.component.scss'],
})
export class FestivalBonusDetailsComponent implements OnInit, OnDestroy {
  festivalBonusDetails?: IFestivalBonusDetails[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  @Input()
  festivalId!: number;

  filterDto = new Filter();

  constructor(
    protected festivalBonusDetailsService: FestivalBonusDetailsService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
   // protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.festivalBonusDetailsService.findByFestivalId(this.festivalId, this.filterDto).subscribe(
      (res: HttpResponse<IFestivalBonusDetails[]>) => {
        this.festivalBonusDetails = res.body!;
      },
      () => this.onError()
    );
  }

  ngOnInit(): void {
    this.loadAll();
    /*this.handleNavigation();
    this.registerChangeInFestivalBonusDetails();*/
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
        this.loadAll(pageNumber, true);
      }
    }).subscribe();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
     // this.eventManager.destroy(this.eventSubscriber);
    }
  }

  holdFestivalBonus(festivalBonusDetail: IFestivalBonusDetails): void {
    swalOnHoldOrUnholdConfirmation('Hold Festival Bonus?').then(result => {
      if (result.isConfirmed) {
        festivalBonusDetail.isHold = true;
        this.festivalBonusDetailsService.holdFbDetails(festivalBonusDetail.id!).subscribe(
          res => {
            swalOnHoldOrUnholdSuccess();
            this.loadAll();
          },
          () => swalOnRequestError()
        );
      }
    });
  }

  unHoldFestivalBonus(festivalBonusDetail: IFestivalBonusDetails): void {
    swalOnHoldOrUnholdConfirmation('Unhold Festival Bonus?').then(result => {
      if (result.isConfirmed) {
        festivalBonusDetail.isHold = false;
        this.festivalBonusDetailsService.unHoldFbDetails(festivalBonusDetail.id!).subscribe(
          res => {
            swalOnHoldOrUnholdSuccess();
            this.loadAll();
          },
          () => swalOnRequestError()
        );
      }
    });
  }

  trackId(index: number, item: IFestivalBonusDetails): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

/*   registerChangeInFestivalBonusDetails(): void {
    this.eventSubscriber = this.eventManager.subscribe('festivalBonusDetailsListModification', () => this.loadAll());
  } */

  delete(festivalBonusDetails: IFestivalBonusDetails): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.festivalBonusDetailsService.delete(festivalBonusDetails.id!).subscribe(
          res => {
            swalOnDeleteSuccess();
            this.loadAll();
          },
          () => swalOnRequestError()
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

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  search($event: any): void {
    this.filterDto.searchText = $event;
    this.loadAll();
  }

}
