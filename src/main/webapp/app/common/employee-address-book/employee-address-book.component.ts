import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, OperatorFunction } from 'rxjs';
import { debounceTime, distinctUntilChanged, map } from 'rxjs/operators';
import { Filter } from './filter.model';
import { EmployeeCommonService } from './employee-common.service';
import { EmployeeSearchService } from './employee-search.service';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { FormControl } from '@angular/forms';
import { SearchModalComponent } from 'app/shared/specialized-search/search-modal/search-modal.component';
import { SearchModalService } from 'app/shared/specialized-search/search-modal/search-modal.service';
import { ParseLinks } from '../../core/util/parse-links.service';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { IEmployee } from '../../entities/employee-custom/employee-custom.model';
import {swalCopyAlert, swalPatternError} from '../../shared/swal-common/swal-common';
import {CustomValidator} from "../../validators/custom-validator";
import {
  SWAL_CONFIRM,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DENY_BTN_TEXT
} from "../../shared/swal-common/swal.properties.constant";
import {DANGER_COLOR, PRIMARY_COLOR} from "../../config/color.code.constant";
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-employee',
  templateUrl: './employee-address-book.component.html',
  styleUrls: ['./employee-address-book.component.scss'],
})
export class EmployeeAddressBookComponent implements OnInit, OnDestroy {
  employees: IEmployee[] = [];

  itemsPerPage = ITEMS_PER_PAGE;
  links: any;
  page!: number;
  predicate!: string;
  ascending!: boolean;

  suggestion: string[] = [];

  filters = new Filter();
  searchText = '';
  searchTextFormControl = new FormControl('',{validators: CustomValidator.naturalTextValidator()});

  currentlyLoadingOrder = 0;

  constructor(
    private searchModalService: SearchModalService,
    public employeeService: EmployeeCommonService,
    protected modalService: NgbModal,
    private employeeSearchService: EmployeeSearchService,
    protected parseLinks: ParseLinks,
    protected applicationConfigService: ApplicationConfigService
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'pin';
    this.ascending = true;
  }

  loadPage(page: number): void {
    // Sometimes when a response takes less time than a previously called one, it gets loaded first.
    // When the previous call finishes it updates the list, replacing the latest result.
    // Hence, the need to check the order of calling.
    this.currentlyLoadingOrder += 1;
    const callingOrder = this.currentlyLoadingOrder;

    this.page = page;

    this.employeeSearchService
      .queryForAddressBook(this.filters, {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(res => {
        if (callingOrder !== this.currentlyLoadingOrder) {
          return;
        }
        this.paginateEmployees(res.body, res.headers);
      }, err => {
        swalPatternError()
      });
  }

  ngOnInit(): void {
    this.loadSuggestions();
    this.loadPage(0);
  }

  ngOnDestroy(): void {
    this.modalService.dismissAll();
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

  search: OperatorFunction<string, readonly string[]> = (text$: Observable<string>) => {
    return text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      map(term => {
        this.suggestion[0] = term;
        if(this.searchTextFormControl.errors?.pattern != true) {
          return term.length < 2
            ? []
            : this.suggestion
              .filter(item => {
                if (item !== undefined && item !== null) {
                  return item.toLowerCase().includes(term.toLowerCase());
                } else {
                  return null;
                }
              })
              .slice(0, 10);
        }
        else return [];
      })
    );
  };

  loadSuggestions(): void {
    this.employeeService.getSuggestions().subscribe(res => {
      this.suggestion = res.body!;
      this.suggestion.unshift('');
    });
  }

  trackId(index: number, item: IEmployee): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  onSearchTextChangeV2(event: any): void {
    /*event.target.value !== this.filters.searchText => means if user search with keyboard enter,
    then do not search again with on change */
    if (event.target.value !== this.filters.searchText && event.target.value.toString().trim().length !== 0) {
      this.filters.searchText = event.target.value;
      this.reset();
    }
  }

  onSearchKeyBoardEnter(event: any): void {
    /* keyup.enter = when enter press, after selecting type_ahead search will work */
    if (event.target.value.toString().trim().length !== 0) {
      this.filters.searchText = event.target.value;
      this.reset();
    }
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'pin') {
      result.push('pin');
    }
    return result;
  }

  getProfilePicture(pin: String): String {
    const resourceUrl = this.applicationConfigService.getEndpointFor('/files/get-employees-image/' + pin);
    return resourceUrl;
  }

  openEmployeeDetailsModal(selectedEmployee: number): void {
    if (selectedEmployee !== undefined) {
      this.searchModalService.find(selectedEmployee).subscribe(response => {
        const modalRef = this.modalService.open(SearchModalComponent, { size: 'xl', backdrop: 'static' });
        modalRef.componentInstance.employeeSpecializedSearch = response.body;
      });
    } else {
      alert(selectedEmployee);
    }
  }

  copyToClipboard(val: string): void {
    const selBox = document.createElement('textarea');
    selBox.style.position = 'fixed';
    selBox.style.left = '0';
    selBox.style.top = '0';
    selBox.style.opacity = '0';
    selBox.value = val;
    document.body.appendChild(selBox);
    selBox.focus();
    selBox.select();
    document.execCommand('copy');
    document.body.removeChild(selBox);
    swalCopyAlert();
  }

  onChangeInput($event: any): void {
    if ($event.target.value.toString().length === 0) {
      this.filters.searchText = '';
      this.reset();
    }
  }

  selectedItem(itemObject: any): void {
    this.filters.searchText = itemObject.item;
    this.reset();
  }

  @HostListener('document:keyup.escape', ['$event']) onKeydownHandler(event: KeyboardEvent): void {
    if(this.searchTextFormControl.errors?.pattern){
      return null
    }
    if (this.searchTextFormControl.value.toString().length !== 0) {
      this.searchTextFormControl.setValue('');
      this.filters.searchText = '';
      this.reset();
    }
  }
}
