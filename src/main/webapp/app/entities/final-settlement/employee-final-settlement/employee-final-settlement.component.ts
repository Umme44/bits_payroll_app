import { AfterViewInit, Component, OnInit } from '@angular/core';
import { EmployeeCommonService } from '../../../shared/service/employee-common.service';
import { DesignationService } from '../../../shared/legacy/legacy-service/designation.service';
import { DepartmentService } from '../../department/service/department.service';
import { EmployeeSearchService } from '../../../common/employee-address-book/employee-search.service';
import { UnitService } from '../../unit/service/unit.service';
import { FinalSettlementService } from '../service/final-settlement.service';
import { ITEMS_PER_PAGE } from '../../../config/pagination.constants';
import { IDesignation } from '../../../shared/legacy/legacy-model/designation.model';
import { IUnit } from '../../../shared/legacy/legacy-model/unit.model';
import { IDepartment } from '../../../shared/legacy/legacy-model/department.model';
import { Filter } from '../../../common/employee-address-book/filter.model';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, Subscription } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import Swal from 'sweetalert2';
import { SWAL_CANCEL_BTN_TEXT, SWAL_CONFIRM_BTN_TEXT, SWAL_SURE } from '../../../shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../../config/color.code.constant';
import { IEmployee } from '../../employee-custom/employee-custom.model';
import {CustomValidator} from "../../../validators/custom-validator";

@Component({
  selector: 'jhi-employee-final-settlement',
  templateUrl: './employee-final-settlement.component.html',
  styleUrls: ['./employee-final-settlement.component.scss'],
})
export class EmployeeFinalSettlementComponent implements OnInit, AfterViewInit {
  employees?: IEmployee[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  designations!: IDesignation[];
  departments!: IDepartment[];
  units!: IUnit[];

  filters = new Filter();

  constructor(
    protected employeeService: EmployeeCommonService,
    private designationService: DesignationService,
    private departmentService: DepartmentService,
    private employeeSearchService: EmployeeSearchService,
    private unitService: UnitService,
    private finalSettlementService: FinalSettlementService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router
  ) {}

  loadAll(): void {
    this.designationService.query().subscribe((res: HttpResponse<IDesignation[]>) => (this.designations = res.body || []));
    this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));
    this.unitService.query().subscribe((res: HttpResponse<IUnit[]>) => (this.units = res.body || []));
  }

  isSearchTextInvalid = false;

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    if(!CustomValidator.NATURAL_TEXT_PATTERN.test(this.filters.searchText)){
      this.isSearchTextInvalid = true;
    }
    else {
      this.isSearchTextInvalid = true;
      this.employeeSearchService
        .queryFinalSettlement(this.filters, {
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IEmployee[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
          () => this.onError()
        );
    }
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

  ngOnInit(): void {
    this.loadAll();
    this.handleNavigation();
  }

  ngAfterViewInit(): void {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const options = {
      beautifyScroll: true, // set to TRUE if you want a beautiful scrollbar
      scrollBarColor: 'yellow', // scrollbar color for making it more beautiful, leave it blank or false
    };
    // customScrollBar.scroll(document.getElementById("container-employee-table"),options);
  }

  trackId(index: number, item: IEmployee): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  // registerChangeInEmployees(): void {
  //   this.eventSubscriber = this.eventManager.subscribe('employeeListModification', () => this.loadPage());
  // }

  reloadDataAsSearchFilter(): void {

    if(!CustomValidator.NATURAL_TEXT_PATTERN.test(this.filters.searchText)){
      this.isSearchTextInvalid = true;
    }
    else{
      this.isSearchTextInvalid = false;
      const pageToLoad = 1;

      this.employeeSearchService
        .queryFinalSettlement(this.filters, {
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          res => this.onSuccess(res.body, res.headers, pageToLoad, true),
          () => this.onError()
        );
    }
  }

  onDesignationSelect(): void {
    this.reloadDataAsSearchFilter();
  }

  onUnitSelect(): void {
    this.reloadDataAsSearchFilter();
  }

  onDepartmentSelect(): void {
    this.reloadDataAsSearchFilter();
  }

  onSearchTextChange(): void {
    this.reloadDataAsSearchFilter();
  }

  protected onSuccess(data: IEmployee[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/final-settlement/employee-select'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.employees = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  getProfilePicture(pin: String): String {
    const resourceUrl = SERVER_API_URL + '/files/get-employees-image/' + pin;
    return resourceUrl;
  }

  generateFinalSettlement(employeeId: number): void {
    Swal.fire({
      text: SWAL_SURE,
      showCancelButton: true,
      confirmButtonColor: PRIMARY_COLOR,
      cancelButtonColor: DANGER_COLOR,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      cancelButtonText: SWAL_CANCEL_BTN_TEXT,
    }).then(result => {
      if (result.isConfirmed) {
        this.generateFinalSettlementConfirmed(employeeId);
      }
    });
  }

  generateFinalSettlementConfirmed(employeeId: number): void {
    this.finalSettlementService.generateAndSaveFinalSettlement(employeeId).subscribe(response => {
      const finalSettlement = response.body;
      if (finalSettlement) {
        this.router.navigate([`/final-settlement/${finalSettlement.id}/view`]);
        Swal.fire({
          icon: 'success',
          html: 'Final Settlement data generation success! <br/> Populating data...',
          showConfirmButton: false,
          timer: 1500,
        });
      }
    });
  }
}
