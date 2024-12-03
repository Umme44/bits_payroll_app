import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IIncomeTaxChallan } from '../income-tax-challan.model';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, IncomeTaxChallanService } from '../service/income-tax-challan.service';
import { IncomeTaxChallanDeleteDialogComponent } from '../delete/income-tax-challan-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { IAitConfig } from '../../ait-config/ait-config.model';
import Swal from 'sweetalert2';
import { swalClose, swalOnDeleteSuccess, swalOnLoading, swalOnRequestError } from 'app/shared/swal-common/swal-common';
import {
  SWAL_CANCEL_BTN_TEXT,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DELETE_CONFIRMATION,
} from '../../../shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../../config/color.code.constant';
import { AitConfigService } from '../../ait-config/service/ait-config.service';

@Component({
  selector: 'jhi-income-tax-challan',
  templateUrl: './income-tax-challan.component.html',
})
export class IncomeTaxChallanComponent implements OnInit {
  incomeTaxChallans?: IIncomeTaxChallan[];
  aitConfigs: IAitConfig[] = [];
  isLoading = false;
  predicate = 'id';
  ascending = true;
  selectedAitConfigId!: number;

  itemsPerPage = ITEMS_PER_PAGE;
  links: { [key: string]: number } = {
    last: 0,
  };
  page = 1;

  constructor(
    protected incomeTaxChallanService: IncomeTaxChallanService,
    protected aitConfigService: AitConfigService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected parseLinks: ParseLinks,
    protected modalService: NgbModal
  ) {}

  reset(): void {
    this.page = 1;
    this.incomeTaxChallans = [];
    this.load();
  }

  loadPage(page: number): void {
    this.page = page;
    this.load();
  }

  trackId = (_index: number, item: IIncomeTaxChallan): number => this.incomeTaxChallanService.getIncomeTaxChallanIdentifier(item);

  ngOnInit(): void {
    this.load();
    this.aitConfigService.query().subscribe(res => (this.aitConfigs = res.body || []));
  }

  delete(incomeTaxChallan: IIncomeTaxChallan): void {
    Swal.fire({
      text: SWAL_DELETE_CONFIRMATION,
      showCancelButton: true,
      confirmButtonColor: PRIMARY_COLOR,
      cancelButtonColor: DANGER_COLOR,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      cancelButtonText: SWAL_CANCEL_BTN_TEXT,
    }).then(result => {
      if (result.isConfirmed) {
        this.incomeTaxChallanService.delete(incomeTaxChallan.id!).subscribe(
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

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
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
    this.incomeTaxChallans = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IIncomeTaxChallan[] | null): IIncomeTaxChallan[] {
    const incomeTaxChallansNew = this.incomeTaxChallans ?? [];
    if (data) {
      for (const d of data) {
        if (incomeTaxChallansNew.map(op => op.id).indexOf(d.id) === -1) {
          incomeTaxChallansNew.push(d);
        }
      }
    }
    return incomeTaxChallansNew;
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
    swalOnLoading('loading...');
    if (this.selectedAitConfigId == null) {
      const queryObject = {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.getSortQueryParam(predicate, ascending),
      };

      return this.incomeTaxChallanService.query(queryObject).pipe(
        tap(() => {
          this.isLoading = false;
          swalClose();
        })
      );
    } else {
      const queryObject = {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.getSortQueryParam(predicate, ascending),
        aitConfigId: this.selectedAitConfigId,
      };
      return this.incomeTaxChallanService.query(queryObject).pipe(
        tap(() => {
          this.isLoading = false;
          swalClose();
        })
      );
    }
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

  onChangeFiscalYear(event: any): void {
    const aitConfigId = event.target.value;
    if (aitConfigId) {
      this.selectedAitConfigId = aitConfigId;
      this.reset(); // load all by searching with aitConfigId
    }
  }
}
