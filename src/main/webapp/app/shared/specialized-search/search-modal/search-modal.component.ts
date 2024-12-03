import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IEmployeeSpecializedSearch } from 'app/shared/specialized-search/search-modal/EmployeeSpecializedSearch.model';
import { SearchModalService } from 'app/shared/specialized-search/search-modal/search-modal.service';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { swalCopyAlert } from '../../swal-common/swal-common';

@Component({
  selector: 'jhi-save-component',
  templateUrl: './search-modal.component.html',
  styleUrls: ['./search-modal.component.scss'],
})
export class SearchModalComponent implements OnInit {
  @Input()
  employeeSpecializedSearch!: IEmployeeSpecializedSearch;

  @Output() passEntry: EventEmitter<boolean> = new EventEmitter();

  constructor(
    private activeModal: NgbActiveModal,
    private searchModalService: SearchModalService,
    protected applicationConfigService: ApplicationConfigService
  ) {}

  ngOnInit(): void {}

  getProfilePicture(pin: String): String {
    const resourceUrl = this.applicationConfigService.getEndpointFor('/files/get-employees-image/' + pin);
    return resourceUrl;
  }

  cancel(): void {
    this.passEntry.emit(false);
    this.activeModal.dismiss();
  }

  confirm(): void {
    this.passEntry.emit(true);
    this.activeModal.dismiss();
  }

  loadEmployee(employeeId: number): void {
    this.searchModalService.find(employeeId).subscribe(response => {
      if (response.body !== null) {
        this.employeeSpecializedSearch = response.body;
      }
    });
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
}
