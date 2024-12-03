import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { IEmployeePin } from '../employee-pin.model';
import { EmployeePinService } from '../service/employee-pin.service';
import { ParseLinks } from '../../../core/util/parse-links.service';
import { EventManager } from '../../../core/util/event-manager.service';
import {
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnRequestError,
  swalOnSavedSuccess,
} from '../../../shared/swal-common/swal-common';
import { swalConfirmationWithMessage } from '../../../shared/swal-common/swal-confirmation.common';
import { EmployeeCategory } from '../../enumerations/employee-category.model';
import {CustomValidator} from "../../../validators/custom-validator";

@Component({
  selector: 'jhi-employee-pin',
  templateUrl: './employee-pin.component.html',
})
export class EmployeePinComponent implements OnInit, OnDestroy {
  employeePins: IEmployeePin[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  selectedCategory: string | undefined;
  selectedStatus: string | undefined;
  searchText = '';

  constructor(
    protected employeePinService: EmployeePinService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    protected router: Router
  ) {
    this.employeePins = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  isSearchTextInvalid = false;
  loadAll(): void {
    this.employeePinService
      .query(this.getReqObject())
      .subscribe((res: HttpResponse<IEmployeePin[]>) => this.paginateEmployeePins(res.body, res.headers));
  }

  getReqObject(): any {
    const obj: any = {
      searchText: this.searchText,
      page: this.page,
      size: this.itemsPerPage,
    };
    if (this.selectedCategory !== 'ALL' && this.selectedCategory !== undefined && this.selectedCategory !== null) {
      obj.selectedCategory = this.selectedCategory;
    }
    if (this.selectedStatus !== 'ALL' && this.selectedStatus !== undefined && this.selectedStatus !== null) {
      obj.selectedStatus = this.selectedStatus;
    }

    return obj;
  }

  reset(): void {
    if(!CustomValidator.NATURAL_TEXT_PATTERN.test(this.searchText)){
      this.isSearchTextInvalid = true;
    }
    else{
      this.isSearchTextInvalid = false
      this.page = 0;
      this.employeePins = [];
      this.loadAll();
    }
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEmployeePins();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEmployeePin): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEmployeePins(): void {
    this.eventSubscriber = this.eventManager.subscribe('employeePinListModification', () => this.reset());
  }

  delete(employeePin: IEmployeePin): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.employeePinService.delete(employeePin.id!).subscribe(
          res => {
            swalOnDeleteSuccess();
            this.reset();
          },
          err => {
            swalOnRequestError();
          }
        );
      }
    });
  }

  // sort(): string[] {
  //   const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
  //   if (this.predicate !== 'id') {
  //     result.push('id');
  //   }
  //   return result;
  // }

  protected paginateEmployeePins(data: IEmployeePin[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.employeePins.push(data[i]);
      }
    }
  }

  onSearchTextChange(event: any): void {
    this.searchText = event.target.value;
    this.reset();
  }

  onSelectEmployeeCategory(event: any): void {
    this.selectedCategory = event.target.value;
    this.reset();
  }

  onChangeEmployeeStatus(event: any): void {
    this.selectedStatus = event.target.value;
    this.reset();
  }

  navigateToEmployeeOnboardPage(pin: string): void {
    this.router.navigate(['/employee-custom/new'], { state: { pin } });
  }

  declineEmployeeOnboard(id: number): void {
    swalConfirmationWithMessage('Are you sure?').then(result => {
      if (result.isConfirmed) {
        this.employeePinService.declineEmployeeOnboard(id).subscribe(
          res => {
            swalOnSavedSuccess();
            this.reset();
          },
          err => {
            swalOnRequestError();
          }
        );
      }
    });
  }

  getEmployeeCategory(category: string): string {
    if (category === EmployeeCategory.CONTRACTUAL_EMPLOYEE) {
      return 'By Contract';
    } else if (category === EmployeeCategory.INTERN) {
      return 'Intern';
    } else {
      return 'Regular';
    }
  }
}
