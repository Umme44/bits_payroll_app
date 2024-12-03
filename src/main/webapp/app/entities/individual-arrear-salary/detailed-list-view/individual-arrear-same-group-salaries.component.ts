import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IIndividualArrearSalary } from '../individual-arrear-salary.model';
import { IEmployeeSalaryGroupData } from '../employee-salary-group-data';
import { IndividualArrearSalaryService } from '../service/individual-arrear-salary.service';
import { ImportDataService } from '../../../admin/import-data/import-data.service';
import { ITEMS_PER_PAGE } from '../../../config/pagination.constants';
import { SuccessMessageComponent } from '../../../shared/success-message/success-message.component';
import { ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-individual-arrear-same-group-salaries',
  templateUrl: './individual-arrear-same-group-salaries.component.html',
})
export class IndividualArrearSameGroupSalariesComponent implements OnInit {
  individualArrearSalaries: IIndividualArrearSalary[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  title!: string;

  isSaving = false;
  private fileToUpload: any;
  uploadReady: boolean;
  individualArrearDataImportSuccess: boolean | null = false;
  individualSalaryDataImportBtnTxt: String = 'Import';
  errorOnIndividualArrearDataImport: boolean | null = false;
  importingIndividualArrearData = false;
  successIndividualArrearData: boolean | null = false;
  employeeSalaryGroupData?: IEmployeeSalaryGroupData[];

  constructor(
    protected individualArrearSalaryService: IndividualArrearSalaryService,
    protected modalService: NgbModal,
    protected importDataService: ImportDataService,
    protected activatedRoute: ActivatedRoute
  ) {
    this.individualArrearSalaries = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
    this.uploadReady = false;
    this.title = activatedRoute.snapshot.params['title'];
  }

  loadAll(): void {
    this.individualArrearSalaryService
      .queryByTitle(this.title, {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IIndividualArrearSalary[]>) => (this.individualArrearSalaries = res.body ?? []));
  }

  reset(): void {
    this.page = 0;
    this.individualArrearSalaries = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IIndividualArrearSalary): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  delete(individualArrearSalary: IIndividualArrearSalary): void {
    Swal.fire({
      text: 'Delete',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes',
    }).then(result => {
      if (result.isConfirmed) {
        this.individualArrearSalaryService.delete(individualArrearSalary.id!).subscribe(() => {
          Swal.fire('Deleted!', '', 'warning');
          this.reset();
        });
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

  // protected paginateIndividualArrearSalaries(data: IIndividualArrearSalary[] | null, headers: HttpHeaders): void {
  //   const headersLink = headers.get('link');
  //   this.links = this.parseLinks.parse(headersLink ? headersLink : '');
  //   if (data) {
  //     for (let i = 0; i < data.length; i++) {
  //       this.individualArrearSalaries.push(data[i]);
  //     }
  //   }
  // }

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
        const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
        modalRef.componentInstance.text = 'Successfully imported all Individual Arrears';
        modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
          if (receivedEntry) {
            this.successIndividualArrearData = this.individualArrearDataImportSuccess;
            this.errorOnIndividualArrearDataImport = !this.individualArrearDataImportSuccess;
            this.individualSalaryDataImportBtnTxt = 'Re Import';
            this.importingIndividualArrearData = false;
          }
        });
      } else {
        const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
        modalRef.componentInstance.text = 'Individual Arrear cannot be imported (bad data format)';
        modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
          if (receivedEntry) {
            this.successIndividualArrearData = this.individualArrearDataImportSuccess;
            this.errorOnIndividualArrearDataImport = !this.individualArrearDataImportSuccess;
            this.individualSalaryDataImportBtnTxt = 'Re Import';
            this.importingIndividualArrearData = false;
          }
        });
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

  protected readonly dayjs = dayjs;
}
