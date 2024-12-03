import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFileTemplates } from '../file-templates.model';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, FileTemplatesService } from '../service/file-templates.service';
import { FileTemplatesDeleteDialogComponent } from '../delete/file-templates-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

import { FormBuilder } from '@angular/forms';
import {
  swalClose,
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnLoading, swalOnRequestError
} from '../../../shared/swal-common/swal-common';
import { ParseLinks } from '../../../core/util/parse-links.service';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import {ILeaveAllocation} from "../../leave-allocation/leave-allocation.model";

@Component({
  selector: 'jhi-file-templates',
  templateUrl: './file-templates.component.html',
  styleUrls:['./file-templates.component.scss']
})
export class FileTemplatesComponent implements OnInit {
  fileTemplates?: IFileTemplates[] = [];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 0;

  ngbPaginationPage = 1;
  srcTxt?: string;
  isSearching = false;
  links: any;
  titles: string[] = [];

  searchForm = this.fb.group({
    searchText: [''],
    fileType: [''],
  });

  constructor(
    protected fileTemplatesService: FileTemplatesService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    private fb: FormBuilder,
    protected parseLinks: ParseLinks
  ) {}

  trackId = (_index: number, item: IFileTemplates): number => this.fileTemplatesService.getFileTemplatesIdentifier(item);

  ngOnInit(): void {
    this.loadAll();

    this.getAllTitles();
  }

  getAllTitles(): void {
    this.fileTemplatesService.getAllTitlesForAdmin().subscribe(res => {
      this.titles = res.body!;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

/*  delete(fileTemplates: IFileTemplates): void {
    const modalRef = this.modalService.open(FileTemplatesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fileTemplates = fileTemplates;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }*/
  delete(templates: IFileTemplates): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.fileTemplatesService
          .delete(templates?.id!).subscribe(
          () => {
            swalOnDeleteSuccess();
            this.loadAll();
          },
          () => swalOnRequestError()
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
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.fileTemplates = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IFileTemplates[] | null): IFileTemplates[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(page?: number, predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.fileTemplatesService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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

  onSearchTextChangeV2(searchText: any): void {
    this.srcTxt = searchText;
    this.isSearching = this.srcTxt === this.searchForm.get('searchText')!.value;
    this.searchForm.get('searchText')!.setValue(searchText);
    if (this.isSearching === false) {
      this.reset();
    }
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  reset(): void {
    this.page = 0;
    this.fileTemplates = [];
    this.loadAll();
  }

  loadAll(): void {
    swalOnLoading('Getting Files');
    this.fileTemplatesService
      .queryAdminFileTemplates({
        page: 0,
        size: this.fileTemplates.length-1,
        sort: this.sort(),
        searchText: this.searchForm.get('searchText')!.value,
        fileType: this.searchForm.get('fileType')!.value,
      })
      .subscribe(
        (res: HttpResponse<IFileTemplates[]>) => {
          swalClose();
          this.fileTemplates=[];
          this.paginateFileTemplates(res.body, res.headers);
        },
        () => this.onError()
      );
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  protected paginateFileTemplates(data: IFileTemplates[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.fileTemplates.push(data[i]);
      }
    }
  }

  sort(): string[] {
    const result = [this.ascending ? 'asc' : 'desc'];
    result.push('id');
    return result;
  }

  filterByFileTypes(): void {
    this.reset();
  }

  export(id: number): void {
    swalOnLoading('Preparing for download...');
    this.fileTemplatesService.downloadAdmin(id).subscribe(
      x => {
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should

        const fileName = this.getFileName(x.headers.get('content-disposition')!);
        const newBlob = new Blob([x.body!], { type: 'application/octet-stream' });

        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob
        if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
          (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
          return;
        }

        // For other browsers:
        // Create a link pointing to the ObjectURL containing the blob.
        const data = window.URL.createObjectURL(newBlob);

        const link = document.createElement('a');
        link.href = data;
        link.download = fileName;
        // this is necessary as link.click() does not work on the latest firefox
        link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

        // tslint:disable-next-line:typedef
        setTimeout(function () {
          // For Firefox it is necessary to delay revoking the ObjectURL
          window.URL.revokeObjectURL(data);
          link.remove();
        }, 100);
      },
      err => {},
      () => {
        swalClose();
      }
    );
  }

  private getFileName(disposition: string): string {
    const utf8FilenameRegex = /filename\*=UTF-8''([\w%\-\\.]+)(?:; ?|$)/i;
    const asciiFilenameRegex = /^filename=(["']?)(.*?[^\\])\1(?:; ?|$)/i;

    let fileName = '';
    if (utf8FilenameRegex.test(disposition)) {
      fileName = decodeURIComponent(utf8FilenameRegex.exec(disposition)![1]);
    } else {
      // prevent ReDos attacks by anchoring the ascii regex to string start and
      //  slicing off everything before 'filename='
      const filenameStart = disposition.toLowerCase().indexOf('filename=');
      if (filenameStart >= 0) {
        const partialDisposition = disposition.slice(filenameStart);
        const matches = asciiFilenameRegex.exec(partialDisposition);
        if (matches != null && matches[2]) {
          fileName = matches[2];
        }
      }
    }
    return fileName;
  }

  getFontAwesomeIcon(fileContentType: string): IconProp {
    if (fileContentType === 'pdf') {
      return 'file-pdf';
    } else if (fileContentType === 'doc' || fileContentType === 'docx') {
      return 'file-word';
    } else if (fileContentType === 'xlsx' || fileContentType === 'xls') {
      return 'file-excel';
    } else if (fileContentType === 'ppt' || fileContentType === 'pptx') {
      return 'file-powerpoint';
    } else {
      return 'file';
    }
  }

  convertToNaturalText(type: any): string {
    return type.toString().charAt(0) + type.toString().toLowerCase().slice(1);
  }
}
