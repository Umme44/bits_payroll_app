import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2';

import { IIndividualArrearSalary } from '../individual-arrear-salary.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ASC, DEFAULT_SORT_DATA, DESC, SORT } from 'app/config/navigation.constants';
import { EntityArrayResponseType, IndividualArrearSalaryService } from '../service/individual-arrear-salary.service';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { IndividualArrearSalaryGroupData } from '../individual-arrear-salary-group-data.model';
import { IEmployeeSalaryGroupData } from '../employee-salary-group-data';
import { ImportDataService } from '../../../admin/import-data/import-data.service';

@Component({
  selector: 'jhi-individual-arrear-salary',
  templateUrl: './individual-arrear-salary.component.html',
})
export class IndividualArrearSalaryComponent implements OnInit {
  individualArrearSalaryGroupData: IndividualArrearSalaryGroupData[] = [];
  individualArrearSalaries?: IIndividualArrearSalary[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  private fileToUpload: any;
  uploadReady: boolean;
  individualArrearDataImportSuccess: boolean | null = false;
  importedFileTitle: string | undefined;
  individualSalaryDataImportBtnTxt: String = 'Import';
  errorOnIndividualArrearDataImport: boolean | null = false;
  importingIndividualArrearData = false;
  successIndividualArrearData: boolean | null = false;
  employeeSalaryGroupData?: IEmployeeSalaryGroupData[];

  itemsPerPage = ITEMS_PER_PAGE;
  links: { [key: string]: number } = {
    last: 0,
  };
  page = 1;

  constructor(
    protected individualArrearSalaryService: IndividualArrearSalaryService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected parseLinks: ParseLinks,
    protected modalService: NgbModal,
    protected importDataService: ImportDataService
  ) {}

  reset(): void {
    this.page = 1;
    this.individualArrearSalaries = [];
    this.load();
  }

  loadPage(page: number): void {
    this.page = page;
    this.load();
  }

  trackId = (_index: number, item: IIndividualArrearSalary): number =>
    this.individualArrearSalaryService.getIndividualArrearSalaryIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  delete(title: any): void {
    /*const modalRef = this.modalService.open(IndividualArrearSalaryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.individualArrearSalary = individualArrearSalary;*/
    Swal.fire({
      text: 'Delete',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes',
    }).then(result => {
      if (result.isConfirmed) {
        this.individualArrearSalaryService.deleteByTitle(title.toString()).subscribe(() => {
          Swal.fire('Deleted!', '', 'warning');
          this.reset();
        });
      }
    });
  }

  load(): void {
    this.individualArrearSalaryService.queryTitleGroups().subscribe(res => {
      this.individualArrearSalaryGroupData = res.body!;
    });
  }

  handleFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.fileToUpload = ev.target.files[0];
      this.uploadReady = true;
    } else {
      this.uploadReady = false;
    }
  }

  importXlsx(type: string): void {
    switch (type) {
      case 'individual_arrear':
        this.uploadIndividualArrearMaster();
        break;
    }
  }

  private uploadIndividualArrearMaster(): void {
    this.importingIndividualArrearData = true;
    this.importDataService.uploadIndividualArrearXlsxFile(this.fileToUpload).subscribe(resp => {
      this.individualArrearDataImportSuccess = resp.body!.uploadSuccess;
      this.successIndividualArrearData = false;
      this.errorOnIndividualArrearDataImport = false;
      if (this.individualArrearDataImportSuccess) {
        this.reset();
        Swal.fire({
          icon: 'success',
          text: 'Uploaded',
          showConfirmButton: false,
          timer: 1500,
        });

        this.successIndividualArrearData = this.individualArrearDataImportSuccess;
        this.errorOnIndividualArrearDataImport = !this.individualArrearDataImportSuccess;
        this.individualSalaryDataImportBtnTxt = 'Re Import';
        this.importingIndividualArrearData = false;
        this.importedFileTitle = resp.body!.title;
      } else {
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'Individual Arrear cannot be imported (bad data format)!',
          showConfirmButton: false,
          timer: 1500,
        });
        this.successIndividualArrearData = this.individualArrearDataImportSuccess;
        this.errorOnIndividualArrearDataImport = !this.individualArrearDataImportSuccess;
        this.individualSalaryDataImportBtnTxt = 'Re Import';
        this.importingIndividualArrearData = false;
      }
    });
  }

  export(title: any): void {
    // window.open('/api/payroll-mgt/export/EmployeeSalary/' + year + '/' + month, '_blank');
    const fileName = title.toString() + '.xlsx';
    this.individualArrearSalaryService.exportIndividualArrearByTitle(title).subscribe(x => {
      // It is necessary to create a new blob object with mime-type explicitly set
      // otherwise only Chrome works like it should
      const newBlob = new Blob([x], { type: 'application/octet-stream' });

      // IE doesn't allow using a blob object directly as link href
      // instead it is necessary to use msSaveOrOpenBlob
      if ((window.navigator as any) && (window.navigator as any).msSaveOrOpenBlob) {
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
    this.individualArrearSalaries = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IIndividualArrearSalary[] | null): IIndividualArrearSalary[] {
    const individualArrearSalariesNew = this.individualArrearSalaries ?? [];
    if (data) {
      for (const d of data) {
        if (individualArrearSalariesNew.map(op => op.id).indexOf(d.id) === -1) {
          individualArrearSalariesNew.push(d);
        }
      }
    }
    return individualArrearSalariesNew;
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
    const queryObject = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.individualArrearSalaryService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
}
