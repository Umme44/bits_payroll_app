import { AfterViewInit, Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { combineLatest, Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Location } from '@angular/common';

import { EmployeeSearchService } from '../../../common/employee-address-book/employee-search.service';

import { ITEMS_PER_PAGE } from '../../../shared/constants/pagination.constants';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { IDesignation } from '../../designation/designation.model';
import { IDepartment } from '../../department/department.model';
import { IUnit } from '../../unit/unit.model';
import { Filter } from '../../../common/employee-address-book/filter.model';
import { EmployeeCustomService } from '../service/employee-custom.service';
import { DesignationService } from '../../designation/service/designation.service';
import { DepartmentService } from '../../department/service/department.service';
import { UnitService } from '../../unit/service/unit.service';
import { EmployeePinService } from '../../employee-pin/service/employee-pin.service';
import { IEmployee } from '../employee-custom.model';
import { EmployeeCustomDeleteDialogComponent } from '../delete/employee-custom-delete-dialog.component';
import {CustomValidator} from "../../../validators/custom-validator";

import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-employee-custom',
  templateUrl: './employee-custom.component.html',
  styleUrls: ['employee-custom.component.scss', '../employee-analytics/employee-analytics.component.scss'],
})
export class EmployeeCustomComponent implements OnInit, AfterViewInit {
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
  isSearchTextInvalid = false;

  constructor(
    protected employeeService: EmployeeCustomService,
    protected modalService: NgbModal,
    private designationService: DesignationService,
    private departmentService: DepartmentService,
    private employeeSearchService: EmployeeSearchService,
    private unitService: UnitService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected location: Location,
    protected employeePinService: EmployeePinService
  ) {}

  loadAll(): void {
    this.designationService.query().subscribe((res: HttpResponse<IDesignation[]>) => (this.designations = res.body || []));
    this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));
    this.unitService.query().subscribe((res: HttpResponse<IUnit[]>) => (this.units = res.body || []));
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    if(!CustomValidator.NATURAL_TEXT_PATTERN.test(this.filters.searchText)){
      this.isSearchTextInvalid = true;
      return
    }
    const pageToLoad: number = page || this.page || 1;

    this.employeeSearchService
      .query(this.filters, {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        res => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
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

  delete(employee: IEmployee): void {
    const modalRef = this.modalService.open(EmployeeCustomDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.employee = employee;
  }

  reloadDataAsSearchFilter(): void {
    const pageToLoad = 1;

    if(!CustomValidator.NATURAL_TEXT_PATTERN.test(this.filters.searchText)){
      this.isSearchTextInvalid = true;
      return
    }

    if (this.filters.departmentId === null) {
      this.filters.departmentId = 0;
    } else if (this.filters.destinationId === null) {
      this.filters.destinationId = 0;
    } else if (this.filters.unitId === null) {
      this.filters.unitId = 0;
    }
    this.employeeSearchService
      .query(this.filters, {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IEmployee[]>) => this.onSuccess(res.body, res.headers, pageToLoad, true),
        () => this.onError()
      );
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
    console.log(this.filters.searchText)
    this.reloadDataAsSearchFilter();
  }

  protected onSuccess(data: IEmployee[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/employee-custom'], {
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

  viewImage(pin: String, name: String): void {
    Swal.fire({
      title: name,
      html: `<img src="` + this.getProfilePicture(pin) + `" style="max-width: 100%; height: auto;">`,
      showCloseButton: true,
      showConfirmButton: false,
      width: '600px',
    });
  }
}
