import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { swalClose, swalOnDeleteSuccess, swalOnLoading, swalOnRequestError } from '../../shared/swal-common/swal-common';
import { ImportEmployeeImageService } from 'app/admin/import-data/import-employee-image.service';
import { SWAL_CANCEL_BTN_TEXT, SWAL_CONFIRM_BTN_TEXT, SWAL_DELETE_CONFIRMATION } from 'app/shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from 'app/shared/constants/color.code.constant';
import { EventManager } from '../../core/util/event-manager.service';
import { ParseLinks } from '../../core/util/parse-links.service';
import { IEmployeeImageUpload } from '../../shared/model/employee-image-upload.model';

@Component({
  selector: 'jhi-import-employee-image',
  templateUrl: './import-employee-image.component.html',
})
export class ImportEmployeeImageComponent implements OnInit, OnDestroy {
  employeeImageUploads: IEmployeeImageUpload[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  searchText = '';

  constructor(
    protected importEmployeeImageService: ImportEmployeeImageService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    protected router: Router
  ) {
    this.employeeImageUploads = [];
    this.itemsPerPage = 10;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  ngOnInit(): void {
    this.loadPage(0);
  }

  ngOnDestroy(): void {}

  loadAll(): void {
    swalOnLoading('Loading Employee Images');
    this.importEmployeeImageService
      .findImageList({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
        searchText: this.searchText,
      })
      .subscribe((res: HttpResponse<IEmployeeImageUpload[]>) => this.paginateEmployeeStaticFiles(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.employeeImageUploads = [];
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

  protected paginateEmployeeStaticFiles(data: IEmployeeImageUpload[] | null, headers: HttpHeaders): void {
    swalClose();
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.employeeImageUploads.push(data[i]);
      }
    }
  }

  delete(pin: any): void {
    Swal.fire({
      text: SWAL_DELETE_CONFIRMATION,
      showCancelButton: true,
      confirmButtonColor: PRIMARY_COLOR,
      cancelButtonColor: DANGER_COLOR,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      cancelButtonText: SWAL_CANCEL_BTN_TEXT,
    }).then(result => {
      if (result.isConfirmed) {
        this.importEmployeeImageService.delete(pin).subscribe(
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

  search($event: any): void {
    this.searchText = $event;
    this.reset();
  }
}
