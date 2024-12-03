import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { IEmployeePinConfiguration } from '../employee-pin-configuration.model';
import { EmployeePinConfigurationService } from '../service/employee-pin-configuration.service';
import { ParseLinks } from '../../../core/util/parse-links.service';
import { EventManager } from '../../../core/util/event-manager.service';
import { swalOnDeleteConfirmation, swalOnDeleteSuccess, swalOnRequestError } from '../../../shared/swal-common/swal-common';
import { EmployeeCategory } from '../../enumerations/employee-category.model';
import {IOfficeNotices} from "../../office-notices/office-notices.model";

@Component({
  selector: 'jhi-employee-pin-configuration',
  templateUrl: './employee-pin-configuration.component.html',
})
export class EmployeePinConfigurationComponent implements OnInit, OnDestroy {
  employeePinConfigurations: IEmployeePinConfiguration[];
  eventSubscriber?: Subscription;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  ngbPaginationPage = 1;

  constructor(
    protected employeePinConfigurationService: EmployeePinConfigurationService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected router: Router,
    protected parseLinks: ParseLinks
  ) {
    this.employeePinConfigurations = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.employeePinConfigurationService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IEmployeePinConfiguration[]>) => this.paginateEmployeePinConfigurations(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.employeePinConfigurations = [];
    this.loadAll();
  }

  // loadPage(page: number): void {
  //   this.page = page;
  //   this.loadAll();
  // }

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.employeePinConfigurationService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IOfficeNotices[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  protected onSuccess(data: IOfficeNotices[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/employee-pin-configuration'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.employeePinConfigurations = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEmployeePinConfigurations();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEmployeePinConfiguration): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEmployeePinConfigurations(): void {
    this.eventSubscriber = this.eventManager.subscribe('employeePinConfigurationListModification', () => this.reset());
  }

  delete(employeePinConfiguration: IEmployeePinConfiguration): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.employeePinConfigurationService.delete(employeePinConfiguration.id!).subscribe(
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

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateEmployeePinConfigurations(data: IEmployeePinConfiguration[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.employeePinConfigurations.push(data[i]);
      }
    }
  }

  getEmployeeCategory(category: string): string {
    if (category === EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE) {
      return 'Regular Provisional';
    } else if (category === EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
      return 'Regular';
    }
    else if (category === EmployeeCategory.CONTRACTUAL_EMPLOYEE) {
      return 'By Contract';
    }
    else if (category === EmployeeCategory.INTERN) {
      return 'Intern';
    } else {
      return 'Regular';
    }
  }
}
