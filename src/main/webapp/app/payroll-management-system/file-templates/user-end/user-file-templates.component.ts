import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder } from '@angular/forms';

import { ITEMS_PER_PAGE } from '../../../shared/constants/pagination.constants';
import { FileTemplatesService } from '../file-templates.service';
import { swalClose, swalOnLoading } from '../../../shared/swal-common/swal-common';

import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { IFileTemplates } from '../../../shared/legacy/legacy-model/file-templates.model';

@Component({
  selector: 'jhi-user-file-templates',
  templateUrl: './user-file-templates.component.html',
  styleUrls: ['./user-file-templates.component.scss'],
})
export class UserFileTemplatesComponent implements OnInit, OnDestroy {
  fileTemplates: IFileTemplates[] = [];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  titles: string[] = [];
  isDuplicateWord = false;
  isDuplicate!: string;

  searchForm = this.fb.group({
    searchText: [''],
    fileType: [''],
  });
  links: any;

  constructor(
    protected fileTemplatesService: FileTemplatesService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    private fb: FormBuilder
  ) {
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'title';
    this.ascending = true;
  }

  ngOnInit(): void {
    this.loadAll();
    this.getAllTitles();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  loadAll(): void {
    swalOnLoading('Getting Files');
    this.fileTemplatesService
      .queryUserFileTemplates({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
        searchText: this.searchForm.get('searchText')!.value,
        fileType: this.searchForm.get('fileType')!.value,
      })
      .subscribe(
        (res: HttpResponse<IFileTemplates[]>) => {
          swalClose();
          this.paginateFileTemplates(res.body, res.headers);
        },
        () => this.onError()
      );
  }

  protected paginateFileTemplates(data: IFileTemplates[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    // this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.fileTemplates.push(data[i]);
      }
    }
  }

  reset(): void {
    this.page = 0;
    this.fileTemplates = [];
    this.loadAll();
  }

  filterByFileTypes(): void {
    this.reset();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      // this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFileTemplates): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  // byteSize(base64String: string): string {
  //   // return this.dataUtils.byteSize(base64String);
  // }

  openFile(contentType = '', base64String: string): void {
    // return this.dataUtils.openFile(contentType, base64String);
  }

  export(id: number): void {
    swalOnLoading('Preparing for download...');
    this.fileTemplatesService.downloadFileCommonUser(id).subscribe(
      x => {
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should

        const fileName = this.getFileName(x.headers.get('content-disposition')!);
        const newBlob = new Blob([x.body!], { type: 'application/octet-stream' });

        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob

        const nav = window.navigator as any;

        if (nav && nav.msSaveOrOpenBlob) {
          nav.msSaveOrOpenBlob(newBlob, fileName);
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

  fileSize(base64String: string): string {
    let n = 0;
    if (base64String.endsWith('==')) {
      n = 2;
    } else if (base64String.endsWith('=')) {
      n = 1;
    } else {
      n = 0;
    }

    const x = base64String.length * (3 / 4) - n;

    let result = '';

    if (x / 1048576 >= 1) {
      result = Math.round(x / 1048576).toFixed(2) + 'MB';
      return result;
    } else if (x / 1024 >= 1) {
      result = Math.round(x / 1024).toFixed(2) + 'KB';
      return result;
    } else {
      result = x + 'Bytes';
      return result;
    }
  }

  // convertToNaturalText(type: FileTemplatesType): string {
  //   return type.toString().charAt(0) + type.toString().toLowerCase().slice(1);
  // }

  onSearchTextChangeV2(searchText: any): void {
    this.isDuplicate = searchText;
    this.isDuplicateWord = this.isDuplicate === this.searchForm.get('searchText')!.value;
    this.searchForm.get('searchText')!.setValue(searchText);
    this.searchForm.get('fileType')!.setValue('');
    if (this.isDuplicateWord === false) {
      this.reset();
    }
  }

  getAllTitles(): void {
    this.fileTemplatesService.getAllTitlesForUser().subscribe(res => {
      this.titles = res.body!;
    });
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
}
