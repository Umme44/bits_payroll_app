import { Component, OnInit, Input, ViewEncapsulation } from '@angular/core';
import { IIncomeTaxStatement } from 'app/shared/model/income-tax-statement-model';
import { Subscription } from 'rxjs';
import { FormBuilder } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from '../../../core/util/event-manager.service';
import { ConfigService } from '../../../entities/config/service/config.service';
import { OrganizationFilesUrl } from '../../../config/organization-files-url';

@Component({
  selector: 'jhi-report-new',
  templateUrl: './report-new.component.html',
  styleUrls: ['./report-new.component.scss'],
  encapsulation: ViewEncapsulation.ShadowDom,
})
export class ReportNewComponent implements OnInit {
  @Input()
  taxStatementModel!: IIncomeTaxStatement;

  eventSubscriber?: Subscription;
  currentYear: number = new Date().getFullYear();

  editForm = this.fb.group({
    year: [],
  });

  public id!: number;

  organizationFullName = '';

  constructor(
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected fb: FormBuilder,
    protected configService: ConfigService
  ) {
    if (sessionStorage.getItem('organizationFullName')) {
      this.organizationFullName = sessionStorage.getItem('organizationFullName')!;
    }
  }

  ngOnInit(): void {}

  getDocumentLetterHead(): string {
    return OrganizationFilesUrl.DOCUMENT_LETTER_HEAD;
  }

  getFinanceManagerSignature(): string {
    return OrganizationFilesUrl.FINANCE_MANAGER_SIGNATURE;
  }

  getOrganizationStamp(): string {
    return OrganizationFilesUrl.ORGANIZATION_STAMP;
  }
}
