import { HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { Filter } from '../employee-address-book/filter.model';
import { EmployeeSearchService } from '../employee-address-book/employee-search.service';

import { ITEMS_PER_PAGE } from '../../config/pagination.constants';
import { ParseLinks } from '../../core/util/parse-links.service';
import { IEmployee } from '../../entities/employee-custom/employee-custom.model';

@Component({
  selector: 'jhi-blood-bank',
  templateUrl: './blood-bank.component.html',
  styleUrls: ['./blood-bank.component.scss'],
})
export class BloodBankComponent implements OnInit {
  employees: IEmployee[] = [];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  filters = new Filter();
  links: any;

  constructor(protected modalService: NgbModal, private employeeSearchService: EmployeeSearchService, private parseLinks: ParseLinks) {
    this.employees = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'pin';
    this.ascending = true;
  }

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.employeeSearchService
      .queryForBloodGroupInfos(this.filters, {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(res => this.paginateEmployees(res.body, res.headers));
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  onSearchTextChangeV2(searchText: any): void {
    this.filters.searchText = searchText;
    this.reset();
  }

  trackId(index: number, item: IEmployee): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  protected paginateEmployees(data: IEmployee[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.employees.push(data[i]);
      }
    }
  }

  reset(): void {
    this.page = 0;
    this.employees = [];
    this.loadPage(this.page);
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'pin') {
      result.push('pin');
    }
    return result;
  }

  gives(bg: any): any {
    if (bg === 'A_POSITIVE') {
      return 'A+,AB+';
    } else if (bg === 'B_POSITIVE') {
      return 'B+, AB+';
    } else if (bg === 'AB_POSITIVE') {
      return 'AB+';
    } else if (bg === 'O_POSITIVE') {
      return 'O+,A+,B+,AB+';
    } else if (bg === 'A_NEGATIVE') {
      return 'A+,A-,AB+,AB-';
    } else if (bg === 'B_NEGATIVE') {
      return 'B+,B-,AB+,AB-';
    } else if (bg === 'O_NEGATIVE') {
      return 'Everyone';
    } else if (bg === 'AB_NEGATIVE') {
      return 'AB+,AB-';
    }
  }

  receives(bg: any): any {
    if (bg === 'A_POSITIVE') {
      return 'A+,A-,O+,O-';
    } else if (bg === 'B_POSITIVE') {
      return 'B+,B-,O+,O-';
    } else if (bg === 'AB_POSITIVE') {
      return 'Everyone';
    } else if (bg === 'O_POSITIVE') {
      return 'O+,O-';
    } else if (bg === 'A_NEGATIVE') {
      return 'A-,O-';
    } else if (bg === 'B_NEGATIVE') {
      return 'B-,O-';
    } else if (bg === 'O_NEGATIVE') {
      return 'O-';
    } else if (bg === 'AB_NEGATIVE') {
      return 'AB-,A-,B-,O-';
    }
  }

  getEmployeeNameList(employees: IEmployee[]): string[] {
    return employees.map(x => {
      return x.fullName!;
    });
  }
}
