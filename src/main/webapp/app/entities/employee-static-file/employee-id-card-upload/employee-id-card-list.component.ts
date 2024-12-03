import { Component, OnDestroy, OnInit } from '@angular/core';
import { IEmployeeStaticFile } from '../../../shared/model/employee-static-file.model';
import { Subscription } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from '../../../shared/constants/pagination.constants';
import { Router } from '@angular/router';
import { swalClose, swalOnDeleteConfirmation, swalOnLoading } from '../../../shared/swal-common/swal-common';
import { EventManager } from '../../../core/util/event-manager.service';
import { ParseLinks } from '../../../core/util/parse-links.service';
import { EmployeeStaticFileService } from '../service/employee-static-file.service';

@Component({
  selector: 'jhi-employee-static-file',
  templateUrl: './employee-id-card-list.component.html',
})
export class EmployeeIdCardListComponent implements OnInit, OnDestroy {
  employeeStaticFiles: IEmployeeStaticFile[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  searchText = '';

  constructor(
    protected employeeStaticFileService: EmployeeStaticFileService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    protected router: Router
  ) {
    this.employeeStaticFiles = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  ngOnInit(): void {
    this.loadAll();
  }

  ngOnDestroy(): void {}

  loadAll(): void {
    swalOnLoading('Loading Employee Id Cards');
    this.employeeStaticFileService
      .findIdCardList({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
        searchText: this.searchText,
      })
      .subscribe((res: HttpResponse<IEmployeeStaticFile[]>) => this.paginateEmployeeStaticFiles(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.employeeStaticFiles = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateEmployeeStaticFiles(data: IEmployeeStaticFile[] | null, headers: HttpHeaders): void {
    swalClose();
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.employeeStaticFiles.push(data[i]);
      }
    }
  }

  delete(id: any): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.employeeStaticFileService.delete(id).subscribe(data => {
          this.reset();
        });
      }
    });
  }

  search($event: any): void {
    this.searchText = $event;
    this.reset();
  }
}
