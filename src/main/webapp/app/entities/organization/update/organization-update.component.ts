import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { DomSanitizer } from '@angular/platform-browser';

import { OrganizationFormGroup, OrganizationFormService } from './organization-form.service';
import { IOrganization } from '../organization.model';
import { OrganizationService } from '../service/organization.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { OrganizationFileType } from '../../../shared/model/enumerations/organization-file-type.enum';
import { IFileDetails } from '../../../shared/legacy/legacy-model/file-details.model';
import { EmployeeCommonService } from '../../../common/employee-address-book/employee-common.service';
import { OrganizationFilesUrl } from '../../../shared/constants/organization-files-url';
import { swalConfirmationWithMessageAndThreeButton } from '../../../shared/swal-common/swal-confirmation.common';
import { swalShowInfoMessage } from 'app/shared/swal-common/swal-common';
import { IEmployee } from '../../employee-custom/employee-custom.model';

@Component({
  selector: 'jhi-organization-update',
  templateUrl: './organization-update.component.html',
  styleUrls: ['./organization-update.component.scss'],
})
export class OrganizationUpdateComponent implements OnInit {
  isSaving = false;
  organization: IOrganization | null = null;

  editForm: OrganizationFormGroup = this.organizationFormService.createOrganizationFormGroup();

  selectedFiles: File[] = [];
  selectedFileTypes: OrganizationFileType[] = [];
  logo!: File;
  employees: IEmployee[] = [];
  stamp!: IFileDetails;

  active = 'basic';
  logoSrc = '';
  filePath!: string;
  financeManagerSignaturePreviewPath!: string;
  logoPreviewPath!: string;
  defaultDocumentLetterHeadPreviewPath!: string;
  pfStatementLetterHeadPreviewPath!: string;
  organizationStampPreviewPath!: string;
  taxDocumentLetterHeadPreviewPath!: string;
  nomineeLetterHeadPreviewPath!: string;
  salaryPayslipLetterHeadPreviewPath!: string;
  festivalBonusPayslipLetterHeadPreviewPath!: string;
  recruitmentRequisitionLetterHeadPreviewPath!: string;

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected organizationService: OrganizationService,
    protected organizationFormService: OrganizationFormService,
    protected activatedRoute: ActivatedRoute,
    protected employeeService: EmployeeCommonService,
    protected domSanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    // this.activatedRoute.data.subscribe(({ organization }) => {
    //   this.organization = organization;
    //   if (organization) {
    //     this.updateForm(organization);
    //   }
    // });
    this.getEmployeeList();
    this.organizationService
      .query({
        size: 1,
      })
      .subscribe(res => {
        if (res.body && res.body.length > 0) {
          this.updateForm(res.body[0]);
        }
      });

    this.getLogoSrc();

    // auto select nav tab after hot reload
    if (sessionStorage.getItem('activeOrganizationTab') !== null && sessionStorage.getItem('activeOrganizationTab') !== undefined) {
      this.active = sessionStorage.getItem('activeOrganizationTab')!;
    }

    this.getStamp();
  }

  ngOnDestroy(): void {
    sessionStorage.removeItem('activeOrganizationTab');
  }

  getEmployeeList(): void {
    this.employeeService.getAllMinimal().subscribe(res => {
      this.employees = res.body || [];

      this.employees = this.employees.map(item => {
        return {
          id: item.id,
          pin: item.pin,
          name: item.fullName,
          designation: item.designationName,
          fullName: item.pin + ' - ' + item.fullName + ' - ' + item.designationName,
        };
      });
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('bitsHrPayrollApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    console.log(this.editForm);
    const organization = this.organizationFormService.getOrganization(this.editForm);
    organization.organizationFileTypeList = this.selectedFileTypes;
    if (organization.id !== null) {
      this.subscribeToSaveResponse(this.organizationService.updateAlongFiles(organization, this.selectedFiles));
    } else {
      this.subscribeToSaveResponse(this.organizationService.createAlongFiles(organization, this.selectedFiles));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganization>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(organization: IOrganization): void {
    this.organization = organization;
    this.organizationFormService.resetForm(this.editForm, organization);
  }

  onChange(event: any, organizationFileType: string): void {
    if (event.target.files.length === 0) return;
    //this.selectedFiles.push(event.target.files[0]);
    //this.selectedFileTypes.push(OrganizationFileType[organizationFileType]);
    this.updateFileList(event.target.files, OrganizationFileType[organizationFileType]);
  }

  updateFileList(fileList: FileList, organizationFileType: OrganizationFileType): void {
    const findIndex = this.selectedFileTypes.findIndex(fileType => fileType === organizationFileType);
    // -1 means not found
    if (findIndex === -1) {
      this.selectedFiles.push(fileList[0]);
      this.selectedFileTypes.push(OrganizationFileType[organizationFileType]);
    } else {
      this.selectedFiles.splice(findIndex, 1, fileList[0]); // replace previous file with new file
      this.selectedFileTypes.splice(findIndex, 1, organizationFileType); // replace previous file type with new
    }
  }

  imagePreview(e: Event, organizationFileType: string): void {
    const file = (e.target as HTMLInputElement).files![0];
    const reader = new FileReader();
    reader.onload = () => {
      const filePath = this.domSanitizer.bypassSecurityTrustUrl(reader.result as string) as string;
      if (OrganizationFileType[organizationFileType] === OrganizationFileType.FINANCE_MANAGER_SIGNATURE) {
        this.financeManagerSignaturePreviewPath = filePath;
      } else if (OrganizationFileType[organizationFileType] === OrganizationFileType.LOGO) {
        this.logoPreviewPath = filePath;
      } else if (OrganizationFileType[organizationFileType] === OrganizationFileType.DOCUMENT_LETTER_HEAD) {
        this.defaultDocumentLetterHeadPreviewPath = filePath;
      } else if (OrganizationFileType[organizationFileType] === OrganizationFileType.ORGANIZATION_STAMP) {
        this.organizationStampPreviewPath = filePath;
      } else if (OrganizationFileType[organizationFileType] === OrganizationFileType.PF_STATEMENT_LETTER_HEAD) {
        this.pfStatementLetterHeadPreviewPath = filePath;
      } else if (OrganizationFileType[organizationFileType] === OrganizationFileType.TAX_STATEMENT_LETTER_HEAD) {
        this.taxDocumentLetterHeadPreviewPath = filePath;
      } else if (OrganizationFileType[organizationFileType] === OrganizationFileType.NOMINEE_LETTER_HEAD) {
        this.nomineeLetterHeadPreviewPath = filePath;
      } else if (OrganizationFileType[organizationFileType] === OrganizationFileType.SALARY_PAYSLIP_LETTER_HEAD) {
        this.salaryPayslipLetterHeadPreviewPath = filePath;
      } else if (OrganizationFileType[organizationFileType] === OrganizationFileType.FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD) {
        this.festivalBonusPayslipLetterHeadPreviewPath = filePath;
      } else if (OrganizationFileType[organizationFileType] === OrganizationFileType.RECRUITMENT_REQUISITION_LETTER_HEAD) {
        this.recruitmentRequisitionLetterHeadPreviewPath = filePath;
      }
    };
    reader.readAsDataURL(file);
  }

  onChangeCommonLetterHead(event: any, organizationFileType: string): void {
    if (event.target.files.length === 0) return;
    swalConfirmationWithMessageAndThreeButton('', 'Do you want to replace all the letter head?', 'No, only this', 'Yes, replace all').then(
      result => {
        if (result.isConfirmed) {
          this.selectedFiles.push(event.target.files[0]);
          this.selectedFileTypes.push(OrganizationFileType[organizationFileType]);
        } else if (result.isDenied) {
          // all letter head
          this.updateFileList(event.target.files, OrganizationFileType.DOCUMENT_LETTER_HEAD);
          this.updateFileList(event.target.files, OrganizationFileType.PF_STATEMENT_LETTER_HEAD);
          this.updateFileList(event.target.files, OrganizationFileType.TAX_STATEMENT_LETTER_HEAD);
          this.updateFileList(event.target.files, OrganizationFileType.NOMINEE_LETTER_HEAD);
          this.updateFileList(event.target.files, OrganizationFileType.SALARY_PAYSLIP_LETTER_HEAD);
          this.updateFileList(event.target.files, OrganizationFileType.FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD);
          this.updateFileList(event.target.files, OrganizationFileType.RECRUITMENT_REQUISITION_LETTER_HEAD);

          swalShowInfoMessage('Now save to view changes!');
        } else if (result.isDismissed) {
          //Swal.fire('you clicked on cancel button', '', 'info')
        }
      }
    );
  }

  getLogoSrc(): void {
    this.logoSrc = OrganizationFilesUrl.LOGO;
  }

  setActiveInSession(event: any): void {
    sessionStorage.setItem('activeOrganizationTab', event.nextId);
  }

  getLetterHead(): string {
    return OrganizationFilesUrl.DOCUMENT_LETTER_HEAD;
  }

  getPfStatementLetterHead(): string {
    return OrganizationFilesUrl.PF_STATEMENT_LETTER_HEAD;
  }

  getTaxStatementLetterHead(): string {
    return OrganizationFilesUrl.TAX_STATEMENT_LETTER_HEAD;
  }

  getNomineeLetterHead(): string {
    return OrganizationFilesUrl.NOMINEE_LETTER_HEAD;
  }

  getSalaryPayslipLetterHead(): string {
    return OrganizationFilesUrl.SALARY_PAYSLIP_LETTER_HEAD;
  }

  getFestivalBonusPayslipLetterHead(): string {
    return OrganizationFilesUrl.FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD;
  }

  getRecruitmentRequisitionLetterHead(): string {
    return OrganizationFilesUrl.RECRUITMENT_REQUISITION_LETTER_HEAD;
  }

  getOrganizationStamp(): string {
    return OrganizationFilesUrl.ORGANIZATION_STAMP;
  }

  getFinanceManagerSignature(): string {
    return OrganizationFilesUrl.FINANCE_MANAGER_SIGNATURE;
  }

  getStamp(): void {
    this.organizationService.getOrganizationFileDetails().subscribe(res => {
      this.stamp = res.body!;
    });
  }
}
