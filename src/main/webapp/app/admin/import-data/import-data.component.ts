import { Component, OnDestroy, OnInit } from '@angular/core';
import { ImportDataService } from './import-data.service';
import { HttpResponse } from '@angular/common/http';
import { SuccessMessageComponent } from '../../shared/success-message/success-message.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {
  swalClose,
  swalForErrorWithMessage,
  swalForWarningWithMessage,
  swalOnAppliedSuccess,
  swalOnBatchDeleteConfirmation,
  swalOnLoading,
  swalOnRequestError,
  swalOnSelection,
  swalOnUpdatedSuccess,
  swalSuccessWithMessage,
} from 'app/shared/swal-common/swal-common';
import { Router } from '@angular/router';
import { swalConfirmationWithMessage } from 'app/shared/swal-common/swal-confirmation.common';
import { ConfigService } from '../../shared/legacy/legacy-service/config.service';
import { EmployeeService } from '../../shared/legacy/legacy-service/employee.service';
import { IConfig } from '../../shared/legacy/legacy-model/config.model';
import { DefinedKeys } from '../../config/defined-keys.constant';

@Component({
  selector: 'jhi-import-data',
  templateUrl: './import-data.component.html',
  styleUrls: ['import-data.component.scss'],
})
export class ImportDataComponent implements OnInit, OnDestroy {
  uploadReady: boolean;
  employeeMasterImportSuccess: boolean | null = false;
  employeeMasterImportBtnTxt: String = 'Import';
  errorOnEmployeeMasterImport: boolean | null = false;
  importingEmployeeMaster = false;
  employeeSuccessMaster: boolean | null = false;

  importingEmployeeLegacy = false;
  employeeLegacyImportSuccess: boolean | null = false;
  employeeLegacyImportBtnTxt: String = 'Import';
  errorOnEmployeeLegacyImport: boolean | null = false;
  employeeSuccessLegacy: boolean | null = false;

  allowanceImportSuccess: boolean | null = false;
  allowanceImportBtnTxt: String = 'Import';
  errorOnAllowanceImport: boolean | null = false;
  importingAllowance = false;

  locationImportSuccess: boolean | null = false;
  locationImportBtnTxt: String = 'Import';
  errorOnLocationImport: boolean | null = false;
  importingLocation = false;

  bankDetailsImportSuccess: boolean | null = false;
  bankDetailsImportBtnTxt: String = 'Import';
  errorOnBankDetailsImport: boolean | null = false;
  importingBankDetails = false;

  holidaysImportSuccess: boolean | null = false;
  holidaysImportBtnTxt: String = 'Import';
  errorOnHolidaysImport: boolean | null = false;
  importingHolidays = false;

  leaveBalanceImportSuccess: boolean | null = false;
  leaveBalanceImportBtnTxt: String = 'Import';
  errorOnLeaveBalanceImport: boolean | null = false;
  importingLeaveBalance = false;

  leaveApplicationImportSuccess: boolean | null = false;
  leaveApplicationImportBtnTxt: String = 'Import';
  errorOnLeaveApplicationImport: boolean | null = false;
  importingLeaveApplication = false;

  attendanceImportSuccess: boolean | null = false;
  attendanceImportBtnTxt: String = 'Import';
  errorOnAttendanceImport: boolean | null = false;
  importingAttendance = false;

  nomineeImportSuccess: boolean | null = false;
  nomineeImportBtnTxt: String = 'Import';
  errorOnNomineeImport: boolean | null = false;
  importingNominee = false;

  previousInsuranceImportBtnTxt: String = 'Import';
  errorOnPreviousInsuranceImport: boolean | null = false;
  previousInsuranceImportSuccess: boolean | null = false;
  importingPreviousInsurance = false;
  previousInsuranceSuccess: boolean | null = false;

  livingAllowanceImportSuccess: boolean | null = false;
  livingAllowanceSuccess: boolean | null = false;
  errorOnLivingAllowanceImport: boolean | null = false;
  livingAllowanceImportBtnTxt: String = 'Import';
  importingLivingAllowance = false;

  rrfImportBtnTxt: String = 'Import';
  errorOnRrfImport: boolean | null = false;
  rrfImportSuccess: boolean | null = false;
  importingRrf = false;
  rrfSuccess: boolean | null = false;

  employeePinImportBtnTxt: String = 'Import';
  errorOnEmployeePinImport: boolean | null = false;
  employeePinImportSuccess: boolean | null = false;
  importingEmployeePin = false;
  employeePinSuccess: boolean | null = false;

  employeeReferencePinImportBtnTxt: String = 'Import';
  errorOnEmployeeReferencePinImport: boolean | null = false;
  employeeReferencePinImportSuccess: boolean | null = false;
  importingEmployeeReferencePin = false;
  employeeReferencePinSuccess: boolean | null = false;

  insuranceClaimImportSuccess: boolean | null = false;
  errorOnInsuranceClaimImport: boolean | null = false;
  insuranceClaimSuccess: boolean | null = false;
  insuranceClaimImportBtnTxt: String = 'Import';
  importingInsuranceClaim = false;

  previousInsuranceClaimImportSuccess: boolean | null = false;
  errorOnPreviousInsuranceClaimImport: boolean | null = false;
  previousInsuranceClaimSuccess: boolean | null = false;
  previousInsuranceClaimImportBtnTxt: String = 'Import';
  importingPreviousInsuranceClaim = false;

  approvedInsuranceRegistrationImportSuccess: boolean | null = false;
  errorOnApprovedInsuranceRegistrationImport: boolean | null = false;
  approvedInsuranceRegistrationSuccess: boolean | null = false;
  approvedInsuranceRegistrationImportBtnTxt: String = 'Import';
  importingApprovedInsuranceRegistration = false;

  movementEntryImportSuccess: boolean | null = false;
  errorOnMovementEntryImport: boolean | null = false;
  movementEntrySuccess: boolean | null = false;
  movementEntryImportBtnTxt: String = 'Import';
  importingMovementEntry = false;

  tinImportSuccess: boolean | null = false;
  errorOnTinImport: boolean | null = false;
  tinSuccess: boolean | null = false;
  tinImportBtnTxt: String = 'Import';
  importingTin = false;

  flexScheduleImportSuccess: boolean | null = false;
  errorOnflexScheduleImport: boolean | null = false;
  flexScheduleSuccess: boolean | null = false;
  flexScheduleImportBtnTxt: String = 'Import';
  importingFlexSchedule = false;

  deletingData = false;
  syncingImages = false;
  forceSyncingImages = false;

  leavebalanceSuccess: boolean | null = false;
  holidaysSuccess: boolean | null = false;
  leaveApplicationSuccess: boolean | null = false;
  attendanceSuccess: boolean | null = false;
  nomineeSuccess: boolean | null = false;

  private fileToUpload: any;
  private employeeLegacyFileToUpload: any;
  private allowanceFileToUpload: any;
  private locationFileToUpload: any;
  private bankDetailsFileToUpload: any;
  private holidaysFileToUpload: any;
  private flexScheduleFileToUpload: any;
  private leaveBalanceFileToUpload: any;
  private leaveApplicationsFileToUpload: any;
  private attendanceFileToUpload: any;
  private nomineeFileToUpload: any;
  private previousInsuranceRegistrationFileToUpload: any;
  private rrfFileToUpload: any;
  private employeePinFileToUpload: any;
  private employeeReferencePinFileToUpload: any;
  private insuranceClaimFileToUpload: any;
  private previousInsuranceClaimFileToUpload: any;
  private approvedInsuranceRegistrationFileToUpload: any;
  private livingAllowanceFileToUpload: any;
  private movementEntryFileToUpload: any;
  private tinFileToUpload: any;

  employeeUploadReady: boolean;
  allowanceUploadReady: boolean;
  locationUploadReady: boolean;
  bankDetailsUploadReady: boolean;
  leaveBalanceUploadReady: boolean;
  leaveApplicationsUploadReady: boolean;
  attendanceUploadReady: boolean;
  nomineeUploadReady: boolean;
  previousInsuranceUploadReady: boolean;
  livingAllowanceUploadReady: boolean;
  movementUploadReady: boolean;
  rrfUploadReady: boolean;
  employeePinUploadReady: boolean;
  employeeReferencePinUploadReady: boolean;
  insuranceClaimUploadReady: boolean;
  previousInsuranceClaimUploadReady: boolean;
  approvedInsuranceRegistrationUploadReady: boolean;
  holidaysUploadReady: boolean;
  flexScheduleUploadReady: boolean;
  tinUploadReady: boolean;

  isLeaveApplicationEnabled = false;
  leaveApplicationEnableConfig!: IConfig;

  constructor(
    protected importDataService: ImportDataService,
    protected employeeService: EmployeeService,
    protected modalService: NgbModal,
    private router: Router,
    private configService: ConfigService
  ) {
    this.uploadReady = false;
    this.employeeUploadReady = false;
    this.allowanceUploadReady = false;
    this.locationUploadReady = false;
    this.bankDetailsUploadReady = false;
    this.leaveBalanceUploadReady = false;
    this.leaveApplicationsUploadReady = false;
    this.attendanceUploadReady = false;
    this.nomineeUploadReady = false;
    this.previousInsuranceUploadReady = false;
    this.livingAllowanceUploadReady = false;
    this.movementUploadReady = false;
    this.rrfUploadReady = false;
    this.employeePinUploadReady = false;
    this.employeeReferencePinUploadReady = false;
    this.insuranceClaimUploadReady = false;
    this.previousInsuranceClaimUploadReady = false;
    this.approvedInsuranceRegistrationUploadReady = false;
    this.holidaysUploadReady = false;
    this.tinUploadReady = false;
    this.flexScheduleUploadReady = false;
  }

  ngOnDestroy(): void {
    swalClose();
  }

  ngOnInit(): void {
    this.configService.findByKey(DefinedKeys.is_leave_application_enabled_for_user_end).subscribe(res => {
      this.leaveApplicationEnableConfig = res.body!;
      if (this.leaveApplicationEnableConfig.value === 'TRUE') this.isLeaveApplicationEnabled = true;
      else this.isLeaveApplicationEnabled = false;
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

  handleEmployeeFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.employeeLegacyFileToUpload = ev.target.files[0];
      this.employeeUploadReady = true;
    } else {
      this.employeeUploadReady = false;
    }
  }

  handleAllowanceFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.allowanceFileToUpload = ev.target.files[0];
      this.allowanceUploadReady = true;
    } else {
      this.allowanceUploadReady = false;
    }
  }

  handleLocationFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.locationFileToUpload = ev.target.files[0];
      this.locationUploadReady = true;
    } else {
      this.locationUploadReady = false;
    }
  }

  handleBankDetailsFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.bankDetailsFileToUpload = ev.target.files[0];
      this.bankDetailsUploadReady = true;
    } else {
      this.bankDetailsUploadReady = false;
    }
  }

  handleHolidaysFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.holidaysFileToUpload = ev.target.files[0];
      this.holidaysUploadReady = true;
    } else {
      this.holidaysUploadReady = false;
    }
  }

  handleFlexScheduleFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.flexScheduleFileToUpload = ev.target.files[0];
      this.flexScheduleUploadReady = true;
    } else {
      this.flexScheduleUploadReady = false;
    }
  }

  handleLeaveBalanceFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.leaveBalanceFileToUpload = ev.target.files[0];
      this.leaveBalanceUploadReady = true;
    } else {
      this.leaveBalanceUploadReady = false;
    }
  }

  handleLeaveApplicationsFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.leaveApplicationsFileToUpload = ev.target.files[0];
      this.leaveApplicationsUploadReady = true;
    } else {
      this.leaveApplicationsUploadReady = false;
    }
  }

  handleAttendanceFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.attendanceFileToUpload = ev.target.files[0];
      this.attendanceUploadReady = true;
    } else {
      this.attendanceUploadReady = false;
    }
  }

  handleNomineesFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.nomineeFileToUpload = ev.target.files[0];
      this.nomineeUploadReady = true;
    } else {
      this.nomineeUploadReady = false;
    }
  }

  handleInsuranceFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.previousInsuranceRegistrationFileToUpload = ev.target.files[0];
      this.previousInsuranceUploadReady = true;
    } else {
      this.previousInsuranceUploadReady = false;
    }
  }
  handleLivingAllowanceFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.livingAllowanceFileToUpload = ev.target.files[0];
      this.livingAllowanceUploadReady = true;
    } else {
      this.livingAllowanceUploadReady = false;
    }
  }

  handleRrfFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.rrfFileToUpload = ev.target.files[0];
      this.rrfUploadReady = true;
    } else {
      this.rrfUploadReady = false;
    }
  }

  handleEmployeePinInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.employeePinFileToUpload = ev.target.files[0];
      this.employeePinUploadReady = true;
    } else {
      this.employeePinUploadReady = false;
    }
  }

  handleEmployeeReferencePinInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.employeeReferencePinFileToUpload = ev.target.files[0];
      this.employeeReferencePinUploadReady = true;
    } else {
      this.employeeReferencePinUploadReady = false;
    }
  }

  handleInsuranceClaimFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.insuranceClaimFileToUpload = ev.target.files[0];
      this.insuranceClaimUploadReady = true;
    } else {
      this.insuranceClaimUploadReady = false;
    }
  }

  handlePreviousInsuranceClaimFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.previousInsuranceClaimFileToUpload = ev.target.files[0];
      this.previousInsuranceClaimUploadReady = true;
    } else {
      this.previousInsuranceClaimUploadReady = false;
    }
  }

  handleApprovedInsuranceRegistrationFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.approvedInsuranceRegistrationFileToUpload = ev.target.files[0];
      this.approvedInsuranceRegistrationUploadReady = true;
    } else {
      this.approvedInsuranceRegistrationUploadReady = false;
    }
  }

  handleMovementEntryFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.movementEntryFileToUpload = ev.target.files[0];
      this.movementUploadReady = true;
    } else {
      this.movementUploadReady = false;
    }
  }

  handleTinFileInput(ev: any): void {
    if (ev.target.files != null && ev.target.files.length > 0) {
      this.tinFileToUpload = ev.target.files[0];
      this.tinUploadReady = true;
    } else {
      this.tinUploadReady = false;
    }
  }

  importXlsx(type: string): void {
    switch (type) {
      case 'employee_master':
        this.uploadEmployeeMaster();
        break;
      case 'employee_common':
        this.uploadEmployeeLegacy();
        break;
      case 'employee_allowance':
        this.uploadEmployeeAllowance();
        break;
      case 'employee_locations':
        this.uploadEmployeeLocation();
        break;
      case 'employee_bank_details':
        this.uploadEmployeeBankDetails();
        break;
      case 'holidays':
        this.uploadHolidays();
        break;
      case 'leave-balance':
        this.uploadLeaveBalance();
        break;
      case 'leave-application':
        this.uploadLeaveApplication();
        break;
      case 'attendance':
        this.uploadAttendance();
        break;
      case 'nominee':
        this.uploadNominee();
        break;
      case 'previous-insurance-registrations':
        this.uploadInsurance();
        break;
      case 'insurance-claim':
        this.uploadInsuranceClaim();
        break;
      case 'previous-insurance-claim':
        this.uploadPreviousInsuranceClaim();
        break;
      case 'livingAllowance':
        this.uploadLivingAllowance();
        break;
      case 'movement-entry':
        this.uploadMovementEntry();
        break;
      case 'rrf':
        this.uploadRrf();
        break;
      case 'employee-pin':
        this.uploadEmployeePin();
        break;
      case 'employee-reference-pin':
        this.uploadEmployeeReferencePin();
        break;
      case 'tin':
        this.uploadTin();
        break;
      case 'flex':
        this.uploadFlexSchedule();
        break;
      case 'approved-insurance-registration':
        this.uploadApprovedInsuranceRegistration();
    }
    // this.uploadReady = false;
  }

  syncImages(force: boolean): void {
    this.syncingImages = !force;
    this.forceSyncingImages = force;

    this.employeeService.syncImages(force).subscribe(
      res => {
        if (res.status === 200) {
          const synced = res.body;
          // alert(`${synced} new images synced`);
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = `${synced} new images synced`;
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              // eslint-disable-next-line @typescript-eslint/no-unused-vars
              const x = 'empty block removal';
            }
          });
        }
        this.syncingImages = this.forceSyncingImages = false;
      },
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      err => {
        // alert('Could not sync images');
        const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
        modalRef.componentInstance.text = 'Could not sync images';
        modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
          if (receivedEntry) {
            this.syncingImages = this.forceSyncingImages = false;
          }
        });
      }
    );
  }

  private uploadEmployeeMaster(): void {
    this.importingEmployeeMaster = true;
    this.importDataService.uploadEmployeeMasterXlsxFile(this.fileToUpload).subscribe((resp: HttpResponse<boolean>) => {
      this.employeeMasterImportSuccess = resp.body;
      this.employeeSuccessMaster = false;
      this.errorOnEmployeeMasterImport = false;
      if (this.employeeMasterImportSuccess) {
        const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
        modalRef.componentInstance.text = 'Successfully imported all employees';
        modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
          if (receivedEntry) {
            this.employeeSuccessMaster = this.employeeMasterImportSuccess;
            this.errorOnEmployeeMasterImport = !this.employeeMasterImportSuccess;
            this.employeeMasterImportBtnTxt = 'Re Import';
            this.importingEmployeeMaster = false;
          }
        });
      } else {
        const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
        modalRef.componentInstance.text = 'Employees cannot be imported (bad data format)';
        modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
          if (receivedEntry) {
            this.employeeSuccessMaster = this.employeeMasterImportSuccess;
            this.errorOnEmployeeMasterImport = !this.employeeMasterImportSuccess;
            this.employeeMasterImportBtnTxt = 'Re Import';
            this.importingEmployeeMaster = false;
          }
        });
      }
    });
  }

  private uploadEmployeeLegacy(): void {
    this.importingEmployeeLegacy = true;
    if (this.employeeLegacyFileToUpload !== undefined && this.employeeUploadReady !== null) {
      this.importDataService.uploadEmployeeLegacyXlsxFile(this.employeeLegacyFileToUpload).subscribe((resp: HttpResponse<boolean>) => {
        this.employeeLegacyImportSuccess = resp.body;
        this.employeeSuccessLegacy = false;
        this.errorOnEmployeeLegacyImport = false;
        if (this.employeeLegacyImportSuccess) {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Successfully imported all employees';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.employeeSuccessLegacy = this.employeeLegacyImportSuccess;
              this.errorOnEmployeeLegacyImport = !this.employeeLegacyImportSuccess;
              this.employeeLegacyImportBtnTxt = 'Re Import';
              this.importingEmployeeLegacy = false;
            }
          });
        } else {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Employees cannot be imported (bad data format)';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.employeeSuccessLegacy = this.employeeLegacyImportSuccess;
              this.errorOnEmployeeLegacyImport = !this.employeeLegacyImportSuccess;
              this.employeeLegacyImportBtnTxt = 'Re Import';
              this.importingEmployeeLegacy = false;
            }
          });
        }
      });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Employees cannot be imported (bad data format)';
      this.errorOnEmployeeLegacyImport = !this.employeeLegacyImportSuccess;
      this.employeeLegacyImportBtnTxt = 'Re Import';
      this.importingEmployeeLegacy = false;
    }
  }

  private uploadEmployeeAllowance(): void {
    this.importingAllowance = true;
    if (this.allowanceFileToUpload !== undefined && this.allowanceFileToUpload !== null) {
      this.importDataService.uploadEmployeeAllowanceXlsxFile(this.allowanceFileToUpload).subscribe((resp: HttpResponse<boolean>) => {
        this.allowanceImportSuccess = resp.body;
        this.errorOnAllowanceImport = false;
        if (this.allowanceImportSuccess) {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Successfully imported Allowances';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.errorOnAllowanceImport = !this.allowanceImportSuccess;
              this.allowanceImportBtnTxt = 'Re Import';
              this.importingAllowance = false;
            }
          });
        } else {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Allowance cannot be imported (bad data format)';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.errorOnAllowanceImport = !this.allowanceImportSuccess;
              this.allowanceImportBtnTxt = 'Re Import';
              this.importingAllowance = false;
            }
          });
        }
      });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Allowance cannot be imported (bad data format)';
      this.errorOnAllowanceImport = !this.allowanceImportSuccess;
      this.allowanceImportBtnTxt = 'Re Import';
      this.importingAllowance = false;
    }
  }

  private uploadEmployeeLocation(): void {
    this.importingLocation = true;
    if (this.locationFileToUpload !== undefined && this.locationFileToUpload !== null) {
      this.importDataService.uploadEmployeeLocationXlsxFile(this.locationFileToUpload).subscribe((resp: HttpResponse<boolean>) => {
        this.locationImportSuccess = resp.body;
        this.errorOnLocationImport = false;
        if (this.locationImportSuccess) {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Successfully imported Locations';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.errorOnLocationImport = !this.locationImportSuccess;
              this.locationImportBtnTxt = 'Re Import';
              this.importingLocation = false;
            }
          });
        } else {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Location cannot be imported (bad data format)';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.errorOnLocationImport = !this.locationImportSuccess;
              this.locationImportBtnTxt = 'Re Import';
              this.importingLocation = false;
            }
          });
        }
      });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Location cannot be imported (bad data format)';
      this.errorOnLocationImport = !this.locationImportSuccess;
      this.locationImportBtnTxt = 'Re Import';
      this.importingLocation = false;
    }
  }

  private uploadEmployeeBankDetails(): void {
    this.importingBankDetails = true;
    if (this.bankDetailsFileToUpload !== undefined && this.bankDetailsFileToUpload !== null) {
      this.importDataService.uploadEmployeeBankDetailsXlsxFile(this.bankDetailsFileToUpload).subscribe((resp: HttpResponse<boolean>) => {
        this.bankDetailsImportSuccess = resp.body;
        this.errorOnBankDetailsImport = false;
        if (this.bankDetailsImportSuccess) {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Successfully imported bank details';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.errorOnBankDetailsImport = !this.bankDetailsImportSuccess;
              this.bankDetailsImportBtnTxt = 'Re Import';
              this.importingBankDetails = false;
            }
          });
        } else {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Bank Details cannot be imported (bad data format)';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.errorOnBankDetailsImport = !this.bankDetailsImportSuccess;
              this.bankDetailsImportBtnTxt = 'Re Import';
              this.importingBankDetails = false;
            }
          });
        }
      });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Allowance cannot be imported (bad data format)';
      this.errorOnBankDetailsImport = !this.bankDetailsImportSuccess;
      this.bankDetailsImportBtnTxt = 'Re Import';
      this.importingBankDetails = false;
    }
  }

  private uploadHolidays(): void {
    this.importingHolidays = true;
    if (this.holidaysFileToUpload !== undefined && this.holidaysFileToUpload !== null) {
      this.importDataService.uploadHolidaysXlsxFile(this.holidaysFileToUpload).subscribe((resp: HttpResponse<boolean>) => {
        this.holidaysImportSuccess = resp.body;
        this.holidaysSuccess = false;
        this.errorOnHolidaysImport = false;
        if (this.holidaysImportSuccess) {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Successfully imported holidays';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.holidaysSuccess = this.holidaysImportSuccess;
              this.errorOnHolidaysImport = !this.holidaysImportSuccess;
              this.holidaysImportBtnTxt = 'Re Import';
              this.importingHolidays = false;
            }
          });
        } else {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Holidays cannot be imported (bad data format)';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.holidaysSuccess = this.holidaysImportSuccess;
              this.errorOnHolidaysImport = !this.holidaysImportSuccess;
              this.holidaysImportBtnTxt = 'Re Import';
              this.importingHolidays = false;
            }
          });
        }
      });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Holidays cannot be imported (bad data format)';
      this.errorOnHolidaysImport = !this.holidaysImportSuccess;
      this.holidaysImportBtnTxt = 'Re Import';
      this.importingHolidays = false;
    }
  }

  private uploadLeaveBalance(): void {
    this.importingLeaveBalance = true;
    if (this.leaveBalanceFileToUpload !== undefined && this.leaveBalanceFileToUpload !== null) {
      this.importDataService.uploadLeaveBalanceXlsxFile(this.leaveBalanceFileToUpload).subscribe((resp: HttpResponse<boolean>) => {
        this.leaveBalanceImportSuccess = resp.body;
        this.leavebalanceSuccess = false;
        this.errorOnLeaveBalanceImport = false;
        if (this.leaveBalanceImportSuccess) {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Successfully imported leave balance';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.leavebalanceSuccess = this.leaveBalanceImportSuccess;
              this.errorOnLeaveBalanceImport = !this.leaveBalanceImportSuccess;
              this.leaveBalanceImportBtnTxt = 'Re Import';
              this.importingLeaveBalance = false;
            }
          });
        } else {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Leave balance cannot be imported (bad data format)';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.leavebalanceSuccess = this.leaveBalanceImportSuccess;
              this.errorOnLeaveBalanceImport = !this.leaveBalanceImportSuccess;
              this.leaveBalanceImportBtnTxt = 'Re Import';
              this.importingLeaveBalance = false;
            }
          });
        }
      });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Leave balance cannot be imported (bad data format)';
      this.errorOnLeaveBalanceImport = !this.leaveBalanceImportSuccess;
      this.leaveBalanceImportBtnTxt = 'Re Import';
      this.importingLeaveBalance = false;
    }
  }

  private uploadLeaveApplication(): void {
    this.importingLeaveApplication = true;
    if (this.leaveApplicationsFileToUpload !== undefined && this.leaveApplicationsFileToUpload !== null) {
      this.importDataService.uploadLeaveApplicationXlsxFile(this.leaveApplicationsFileToUpload).subscribe((resp: HttpResponse<boolean>) => {
        this.leaveApplicationImportSuccess = resp.body;
        this.leaveApplicationSuccess = false;
        this.errorOnLeaveApplicationImport = false;
        if (this.leaveApplicationImportSuccess) {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Successfully imported leave Applications';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.leaveApplicationSuccess = this.leaveApplicationImportSuccess;
              this.errorOnLeaveApplicationImport = !this.leaveApplicationImportSuccess;
              this.leaveApplicationImportBtnTxt = 'Re Import';
              this.importingLeaveApplication = false;
            }
          });
        } else {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Leave Application cannot be imported (bad data format)';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.leaveApplicationSuccess = this.leaveApplicationImportSuccess;
              this.errorOnLeaveApplicationImport = !this.leaveApplicationImportSuccess;
              this.leaveApplicationImportBtnTxt = 'Re Import';
              this.importingLeaveApplication = false;
            }
          });
        }
      });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Leave Application cannot be imported (bad data format)';
      this.errorOnLeaveApplicationImport = !this.leaveApplicationImportSuccess;
      this.leaveApplicationImportBtnTxt = 'Re Import';
      this.importingLeaveApplication = false;
    }
  }

  private uploadAttendance(): void {
    this.importingAttendance = true;
    if (this.attendanceFileToUpload !== undefined && this.attendanceFileToUpload !== null) {
      this.importDataService.uploadAttendanceXlsxFile(this.attendanceFileToUpload).subscribe((resp: HttpResponse<boolean>) => {
        this.attendanceImportSuccess = resp.body;
        this.attendanceSuccess = false;
        this.errorOnAttendanceImport = false;
        if (this.attendanceImportSuccess) {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Successfully imported leave Applications';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.attendanceSuccess = this.attendanceImportSuccess;
              this.errorOnAttendanceImport = !this.attendanceImportSuccess;
              this.attendanceImportBtnTxt = 'Re Import';
              this.importingAttendance = false;
            }
          });
        } else {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Attendance cannot be imported (bad data format)';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.attendanceSuccess = this.attendanceImportSuccess;
              this.errorOnAttendanceImport = !this.attendanceImportSuccess;
              this.attendanceImportBtnTxt = 'Re Import';
              this.importingAttendance = false;
            }
          });
        }
      });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Attendance cannot be imported (bad data format)';
      this.errorOnAttendanceImport = !this.attendanceImportSuccess;
      this.attendanceImportBtnTxt = 'Re Import';
      this.importingAttendance = false;
    }
  }

  private uploadNominee(): void {
    this.importingNominee = true;
    if (this.nomineeFileToUpload !== undefined && this.nomineeFileToUpload !== null) {
      this.importDataService.uploadNomineeXlsxFile(this.nomineeFileToUpload).subscribe((resp: HttpResponse<boolean>) => {
        this.nomineeImportSuccess = resp.body;
        this.nomineeSuccess = false;
        this.errorOnNomineeImport = false;
        if (this.nomineeImportSuccess) {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Successfully imported Nominee';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.nomineeSuccess = this.nomineeImportSuccess;
              this.errorOnNomineeImport = !this.nomineeImportSuccess;
              this.nomineeImportBtnTxt = 'Re Import';
              this.importingNominee = false;
            }
          });
        } else {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Nominee cannot be imported (bad data format)';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.nomineeSuccess = this.nomineeImportSuccess;
              this.errorOnNomineeImport = !this.nomineeImportSuccess;
              this.nomineeImportBtnTxt = 'Re Import';
              this.importingNominee = false;
            }
          });
        }
      });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Nominee cannot be imported (bad data format)';
      this.errorOnNomineeImport = !this.nomineeImportSuccess;
      this.nomineeImportBtnTxt = 'Re Import';
      this.importingNominee = false;
    }
  }

  private uploadInsurance(): void {
    this.importingPreviousInsurance = true;
    if (this.previousInsuranceRegistrationFileToUpload !== undefined && this.previousInsuranceRegistrationFileToUpload !== null) {
      this.importDataService
        .uploadPreviousInsuranceRegistrationsXlsxFile(this.previousInsuranceRegistrationFileToUpload)
        .subscribe((resp: HttpResponse<boolean>) => {
          this.previousInsuranceImportSuccess = resp.body;
          this.previousInsuranceSuccess = false;
          this.errorOnPreviousInsuranceImport = false;
          if (this.previousInsuranceImportSuccess) {
            const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
            modalRef.componentInstance.text = 'Successfully imported Insurance';
            modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
              if (receivedEntry) {
                this.previousInsuranceSuccess = this.previousInsuranceImportSuccess;
                this.errorOnPreviousInsuranceImport = !this.previousInsuranceImportSuccess;
                this.previousInsuranceImportBtnTxt = 'Re Import';
                this.importingPreviousInsurance = false;
              }
            });
          } else {
            const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
            modalRef.componentInstance.text = 'Insurance registrations cannot be imported (bad data format)';
            modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
              if (receivedEntry) {
                this.previousInsuranceSuccess = this.previousInsuranceImportSuccess;
                this.errorOnPreviousInsuranceImport = !this.previousInsuranceImportSuccess;
                this.previousInsuranceImportBtnTxt = 'Re Import';
                this.importingPreviousInsurance = false;
              }
            });
          }
        });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Insurance registrations cannot be imported (bad data format)';
      this.errorOnPreviousInsuranceImport = !this.previousInsuranceImportSuccess;
      this.previousInsuranceImportBtnTxt = 'Re Import';
      this.importingPreviousInsurance = false;
    }
  }

  private uploadApprovedInsuranceRegistration(): void {
    this.importingApprovedInsuranceRegistration = true;
    if (this.approvedInsuranceRegistrationFileToUpload !== undefined && this.approvedInsuranceRegistrationFileToUpload !== null) {
      this.importDataService
        .uploadApprovedInsuranceRegistrationsXlsxFile(this.approvedInsuranceRegistrationFileToUpload)
        .subscribe((resp: HttpResponse<boolean>) => {
          this.approvedInsuranceRegistrationImportSuccess = resp.body;
          this.approvedInsuranceRegistrationSuccess = false;
          this.errorOnApprovedInsuranceRegistrationImport = false;
          if (this.approvedInsuranceRegistrationImportSuccess) {
            const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
            modalRef.componentInstance.text = 'Successfully imported Insurance Registrations';
            modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
              if (receivedEntry) {
                this.approvedInsuranceRegistrationSuccess = this.approvedInsuranceRegistrationImportSuccess;
                this.errorOnApprovedInsuranceRegistrationImport = !this.approvedInsuranceRegistrationImportSuccess;
                this.approvedInsuranceRegistrationImportBtnTxt = 'Re Import';
                this.importingApprovedInsuranceRegistration = false;
              }
            });
          } else {
            const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
            modalRef.componentInstance.text = 'Insurance registrations cannot be imported (bad data format)';
            modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
              if (receivedEntry) {
                this.approvedInsuranceRegistrationSuccess = this.approvedInsuranceRegistrationImportSuccess;
                this.errorOnApprovedInsuranceRegistrationImport = !this.approvedInsuranceRegistrationImportSuccess;
                this.approvedInsuranceRegistrationImportBtnTxt = 'Re Import';
                this.importingApprovedInsuranceRegistration = false;
              }
            });
          }
        });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Insurance registrations cannot be imported (bad data format)';
      this.errorOnApprovedInsuranceRegistrationImport = !this.approvedInsuranceRegistrationImportSuccess;
      this.approvedInsuranceRegistrationImportBtnTxt = 'Re Import';
      this.importingApprovedInsuranceRegistration = false;
    }
  }

  private uploadRrf(): void {
    this.importingRrf = true;
    if (this.rrfFileToUpload !== undefined && this.rrfFileToUpload !== null) {
      this.importDataService.uploadRrfXlsxFile(this.rrfFileToUpload).subscribe((resp: HttpResponse<boolean>) => {
        this.rrfImportSuccess = resp.body;
        this.rrfSuccess = false;
        this.errorOnRrfImport = false;
        if (this.rrfImportSuccess) {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Successfully imported Recruitment Requisition';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.rrfSuccess = this.rrfImportSuccess;
              this.errorOnRrfImport = !this.rrfImportSuccess;
              this.rrfImportBtnTxt = 'Re Import';
              this.importingRrf = false;
            }
          });
        } else {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Recruitment Requisition cannot be imported (bad data format)';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.rrfSuccess = this.rrfImportSuccess;
              this.errorOnRrfImport = !this.rrfImportSuccess;
              this.rrfImportBtnTxt = 'Re Import';
              this.importingRrf = false;
            }
          });
        }
      });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Recruitment Requisition cannot be imported (bad data format)';
      this.errorOnRrfImport = !this.rrfImportSuccess;
      this.rrfImportBtnTxt = 'Re Import';
      this.importingRrf = false;
    }
  }

  private uploadEmployeePin(): void {
    this.importingEmployeePin = true;
    if (this.employeePinFileToUpload !== undefined && this.employeePinFileToUpload !== null) {
      this.importDataService.uploadEmployeePinXlsxFile(this.employeePinFileToUpload).subscribe((resp: HttpResponse<boolean>) => {
        this.employeePinImportSuccess = resp.body;
        this.employeePinSuccess = false;
        this.errorOnEmployeePinImport = false;
        if (this.employeePinImportSuccess) {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Successfully imported Employee PIN';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.employeePinSuccess = this.employeePinImportSuccess;
              this.errorOnEmployeePinImport = !this.employeePinImportSuccess;
              this.employeePinImportBtnTxt = 'Re Import';
              this.importingEmployeePin = false;
            }
          });
        } else {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Recruitment Requisition cannot be imported (bad data format)';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.employeePinSuccess = this.employeePinImportSuccess;
              this.errorOnEmployeePinImport = !this.employeePinImportSuccess;
              this.employeePinImportBtnTxt = 'Re Import';
              this.importingEmployeePin = false;
            }
          });
        }
      });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Employee PIN cannot be imported (bad data format)';
      this.errorOnEmployeePinImport = !this.employeePinImportSuccess;
      this.employeePinImportBtnTxt = 'Re Import';
      this.importingEmployeePin = false;
    }
  }

  private uploadEmployeeReferencePin(): void {
    this.importingEmployeeReferencePin = true;
    if (this.employeeReferencePinFileToUpload !== undefined && this.employeeReferencePinFileToUpload !== null) {
      this.importDataService
        .uploadEmployeeReferencePinXlsxFile(this.employeeReferencePinFileToUpload)
        .subscribe((resp: HttpResponse<boolean>) => {
          this.employeeReferencePinImportSuccess = resp.body;
          this.employeeReferencePinSuccess = false;
          this.errorOnEmployeeReferencePinImport = false;
          if (this.employeeReferencePinImportSuccess) {
            const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
            modalRef.componentInstance.text = 'Successfully imported Employee PIN';
            modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
              if (receivedEntry) {
                this.employeeReferencePinSuccess = this.employeeReferencePinImportSuccess;
                this.errorOnEmployeeReferencePinImport = !this.employeeReferencePinImportSuccess;
                this.employeeReferencePinImportBtnTxt = 'Re Import';
                this.importingEmployeeReferencePin = false;
              }
            });
          } else {
            const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
            modalRef.componentInstance.text = 'Recruitment Requisition cannot be imported (bad data format)';
            modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
              if (receivedEntry) {
                this.employeeReferencePinSuccess = this.employeeReferencePinImportSuccess;
                this.errorOnEmployeeReferencePinImport = !this.employeeReferencePinImportSuccess;
                this.employeeReferencePinImportBtnTxt = 'Re Import';
                this.importingEmployeeReferencePin = false;
              }
            });
          }
        });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Employee Reference PIN cannot be imported (bad data format)';
      this.errorOnEmployeeReferencePinImport = !this.employeeReferencePinImportSuccess;
      this.employeeReferencePinImportBtnTxt = 'Re Import';
      this.importingEmployeeReferencePin = false;
    }
  }

  private uploadInsuranceClaim(): void {
    this.importingInsuranceClaim = true;
    if (this.insuranceClaimFileToUpload !== undefined && this.insuranceClaimFileToUpload !== null) {
      this.importDataService.uploadInsuranceClaimXlsxFile(this.insuranceClaimFileToUpload).subscribe((resp: HttpResponse<boolean>) => {
        this.insuranceClaimImportSuccess = resp.body;
        this.insuranceClaimSuccess = false;
        this.errorOnInsuranceClaimImport = false;
        if (this.insuranceClaimImportSuccess) {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Successfully imported Insurance Claims';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.insuranceClaimSuccess = this.insuranceClaimImportSuccess;
              this.errorOnInsuranceClaimImport = !this.insuranceClaimImportSuccess;
              this.insuranceClaimImportBtnTxt = 'Re Import';
              this.importingInsuranceClaim = false;
            }
          });
        } else {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Insurance Claims cannot be imported (bad data format)';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.insuranceClaimSuccess = this.insuranceClaimImportSuccess;
              this.errorOnInsuranceClaimImport = !this.insuranceClaimImportSuccess;
              this.insuranceClaimImportBtnTxt = 'Re Import';
              this.importingInsuranceClaim = false;
            }
          });
        }
      });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Insurance Claims cannot be imported (bad data format)';
      this.errorOnInsuranceClaimImport = !this.insuranceClaimImportSuccess;
      this.insuranceClaimImportBtnTxt = 'Re Import';
      this.importingInsuranceClaim = false;
    }
  }

  private uploadPreviousInsuranceClaim(): void {
    this.importingPreviousInsuranceClaim = true;
    if (this.previousInsuranceClaimFileToUpload !== undefined && this.previousInsuranceClaimFileToUpload !== null) {
      this.importDataService
        .uploadPreviousInsuranceClaimXlsxFile(this.previousInsuranceClaimFileToUpload)
        .subscribe((resp: HttpResponse<boolean>) => {
          this.previousInsuranceClaimImportSuccess = resp.body;
          this.previousInsuranceClaimSuccess = false;
          this.errorOnPreviousInsuranceClaimImport = false;
          if (this.previousInsuranceClaimImportSuccess) {
            const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
            modalRef.componentInstance.text = 'Successfully imported Insurance Claims';
            modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
              if (receivedEntry) {
                this.previousInsuranceClaimSuccess = this.previousInsuranceClaimImportSuccess;
                this.errorOnPreviousInsuranceClaimImport = !this.previousInsuranceClaimImportSuccess;
                this.previousInsuranceClaimImportBtnTxt = 'Re Import';
                this.importingPreviousInsuranceClaim = false;
              }
            });
          } else {
            const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
            modalRef.componentInstance.text = 'Insurance Claims cannot be imported (bad data format)';
            modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
              if (receivedEntry) {
                this.previousInsuranceClaimSuccess = this.previousInsuranceClaimImportSuccess;
                this.errorOnPreviousInsuranceClaimImport = !this.previousInsuranceClaimImportSuccess;
                this.previousInsuranceClaimImportBtnTxt = 'Re Import';
                this.importingPreviousInsuranceClaim = false;
              }
            });
          }
        });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Insurance Claims cannot be imported (bad data format)';
      this.errorOnPreviousInsuranceClaimImport = !this.previousInsuranceClaimImportSuccess;
      this.previousInsuranceClaimImportBtnTxt = 'Re Import';
      this.importingPreviousInsuranceClaim = false;
    }
  }

  private uploadLivingAllowance(): void {
    this.importingLivingAllowance = true;
    if (this.livingAllowanceFileToUpload !== undefined && this.livingAllowanceFileToUpload !== null) {
      this.importDataService.uploadLivingAllowanceXlsxFile(this.livingAllowanceFileToUpload).subscribe((resp: HttpResponse<boolean>) => {
        this.livingAllowanceImportSuccess = resp.body;
        this.livingAllowanceSuccess = false;
        this.errorOnLivingAllowanceImport = false;
        if (this.livingAllowanceImportSuccess) {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Successfully imported living Allowance';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.livingAllowanceSuccess = this.livingAllowanceImportSuccess;
              this.errorOnLivingAllowanceImport = !this.livingAllowanceImportSuccess;
              this.livingAllowanceImportBtnTxt = 'Re Import';
              this.importingLivingAllowance = false;
            }
          });
        } else {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Living allowance cannot be imported (bad data format)';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.livingAllowanceSuccess = this.livingAllowanceImportSuccess;
              this.errorOnLivingAllowanceImport = !this.livingAllowanceImportSuccess;
              this.livingAllowanceImportBtnTxt = 'Re Import';
              this.importingLivingAllowance = false;
            }
          });
        }
      });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Living allowance cannot be imported (bad data format)';
      this.errorOnLivingAllowanceImport = !this.livingAllowanceImportSuccess;
      this.livingAllowanceImportBtnTxt = 'Re Import';
      this.importingLivingAllowance = false;
    }
  }

  private uploadMovementEntry(): void {
    this.importingMovementEntry = true;
    if (this.movementEntryFileToUpload !== undefined && this.movementEntryFileToUpload !== null) {
      this.importDataService.uploadMovementEntryXlsxFile(this.movementEntryFileToUpload).subscribe((resp: HttpResponse<boolean>) => {
        this.movementEntryImportSuccess = resp.body;
        this.movementEntrySuccess = false;
        this.errorOnMovementEntryImport = false;
        if (this.movementEntryImportSuccess) {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Successfully imported movement entry';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.movementEntrySuccess = this.movementEntryImportSuccess;
              this.errorOnMovementEntryImport = !this.movementEntryImportSuccess;
              this.movementEntryImportBtnTxt = 'Re Import';
              this.importingMovementEntry = false;
            }
          });
        } else {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Movement entry cannot be imported (bad data format)';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.movementEntrySuccess = this.movementEntryImportSuccess;
              this.errorOnMovementEntryImport = !this.movementEntryImportSuccess;
              this.movementEntryImportBtnTxt = 'Re Import';
              this.importingMovementEntry = false;
            }
          });
        }
      });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Movement entry cannot be imported (bad data format)';
      this.errorOnMovementEntryImport = !this.movementEntryImportSuccess;
      this.movementEntryImportBtnTxt = 'Re Import';
      this.importingMovementEntry = false;
    }
  }

  private uploadTin(): void {
    this.importingTin = true;
    if (this.tinFileToUpload !== undefined && this.tinFileToUpload !== null) {
      this.importDataService.uploadTinXlsxFile(this.tinFileToUpload).subscribe((resp: HttpResponse<boolean>) => {
        this.tinImportSuccess = resp.body;
        this.tinSuccess = false;
        this.errorOnTinImport = false;
        if (this.tinImportSuccess) {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Successfully imported Employee TIN Number';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.tinSuccess = this.tinImportSuccess;
              this.errorOnTinImport = !this.tinImportSuccess;
              this.tinImportBtnTxt = 'Re Import';
              this.importingTin = false;
            }
          });
        } else {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Employee TIN cannot be imported (bad data format)';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.tinSuccess = this.tinImportSuccess;
              this.errorOnTinImport = !this.tinImportSuccess;
              this.tinImportBtnTxt = 'Re Import';
              this.importingTin = false;
            }
          });
        }
      });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Employee TIN cannot be imported (bad data format)';
      this.errorOnTinImport = !this.tinImportSuccess;
      this.tinImportBtnTxt = 'Re Import';
      this.importingTin = false;
    }
  }

  private uploadFlexSchedule(): void {
    this.importingFlexSchedule = true;
    if (this.flexScheduleFileToUpload !== undefined && this.flexScheduleFileToUpload !== null) {
      this.importDataService.uploadFlexScheduleXlsxFile(this.flexScheduleFileToUpload).subscribe((resp: HttpResponse<boolean>) => {
        this.flexScheduleImportSuccess = resp.body;
        this.importingFlexSchedule = false;
        this.errorOnflexScheduleImport = false;
        if (this.flexScheduleImportSuccess) {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Successfully imported Flex Schedule';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.flexScheduleSuccess = this.flexScheduleImportSuccess;
              this.errorOnflexScheduleImport = !this.flexScheduleImportSuccess;
              this.flexScheduleImportBtnTxt = 'Re Import';
              this.importingFlexSchedule = false;
            }
          });
        } else {
          const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
          modalRef.componentInstance.text = 'Flex Schedule cannot be imported (bad data format)';
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
            if (receivedEntry) {
              this.flexScheduleSuccess = this.flexScheduleImportSuccess;
              this.errorOnflexScheduleImport = !this.flexScheduleSuccess;
              this.flexScheduleImportBtnTxt = 'Re Import';
              this.importingFlexSchedule = false;
            }
          });
        }
      });
    } else {
      const modalRef = this.modalService.open(SuccessMessageComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.text = 'Flex Schedule cannot be imported (bad data format)';
      this.errorOnflexScheduleImport = !this.flexScheduleSuccess;
      this.flexScheduleImportBtnTxt = 'Re Import';
      this.importingFlexSchedule = false;
    }
  }

  selectNomineeInfo(): void {
    swalOnSelection('General & GF Nominee', 'PF Nominee').then(result => {
      if (result.isConfirmed) {
        this.router.navigate(['nominee']);
      } else {
        this.router.navigate(['pf-nominee']);
      }
    });
  }

  sendSampleEmail(): void {
    this.importDataService.sendSampleEmail().subscribe(
      res => {
        if (res.body! === true) {
          swalSuccessWithMessage('Mail Sent Successfully');
        }
      },
      () => {
        swalForWarningWithMessage('Mail Configuration Failed!');
      }
    );
  }

  requestZeroHourScheduler(): void {
    this.importDataService.requestZeroHourScheduler().subscribe(
      () => {
        swalSuccessWithMessage('Request Success! Action on Progress...');
      },
      () => {
        swalForWarningWithMessage('Failed to Request!');
      }
    );
  }

  requestMorningScheduler(): void {
    this.importDataService.requestMorningScheduler().subscribe(
      () => {
        swalSuccessWithMessage('Request Success! Action on Progress...');
      },
      () => {
        swalForWarningWithMessage('Failed to Request!');
      }
    );
  }

  requestToDeleteInsuranceRegistrationData(): void {
    const warningMessage = 'Are You Sure You Want To Delete All Insurance Data?';
    swalOnBatchDeleteConfirmation(warningMessage).then(result => {
      if (result.isConfirmed) {
        this.deletingData = true;
        this.importDataService.requestToDeleteInsuranceData().subscribe(res => {
          if (res.body! === true) {
            swalSuccessWithMessage('Successfully Deleted All Insurance Data!');
            this.deletingData = false;
          } else {
            swalForWarningWithMessage('Request Failed!');
            this.deletingData = false;
          }
        });
      }
    });
  }

  requestToDeletePfNomineeData(): void {
    const warningMessage = 'Are You Sure You Want To Delete All PF Nominee Data?';
    swalOnBatchDeleteConfirmation(warningMessage).then(result => {
      if (result.isConfirmed) {
        this.deletingData = true;
        this.importDataService.requestToDeletePfNomineeData().subscribe(res => {
          if (res.body! === true) {
            swalSuccessWithMessage('Successfully Deleted All PF Nominee Data!');
            this.deletingData = false;
          } else {
            swalForWarningWithMessage('Request Failed!');
            this.deletingData = false;
          }
        });
      }
    });
  }

  requestToDeleteGfNomineeData(): void {
    const warningMessage = 'Are You Sure You Want To Delete All GF Nominee Data?';
    swalOnBatchDeleteConfirmation(warningMessage).then(result => {
      if (result.isConfirmed) {
        this.deletingData = true;
        this.importDataService.requestToDeleteGfNomineeData().subscribe(res => {
          if (res.body! === true) {
            swalSuccessWithMessage('Successfully Deleted All GF Nominee Data!');
            this.deletingData = false;
          } else {
            swalForWarningWithMessage('Request Failed!');
            this.deletingData = false;
          }
        });
      }
    });
  }

  requestToDeleteGeneralNomineeData(): void {
    const warningMessage = 'Are You Sure You Want To Delete All General Nominee Data?';
    swalOnBatchDeleteConfirmation(warningMessage).then(result => {
      if (result.isConfirmed) {
        this.deletingData = true;
        this.importDataService.requestToDeleteGeneralNomineeData().subscribe(res => {
          if (res.body! === true) {
            swalSuccessWithMessage('Successfully Deleted All General Nominee Data!');
            this.deletingData = false;
          } else {
            swalForWarningWithMessage('Request Failed!');
            this.deletingData = false;
          }
        });
      }
    });
  }

  requestToChangeRRFStatusFromClosedToOpen(): void {
    const warningMessage = 'Are You Sure You?';
    swalOnBatchDeleteConfirmation(warningMessage).then(result => {
      if (result.isConfirmed) {
        this.deletingData = true;
        this.importDataService.changeRRFStatusFromClosedToOpen().subscribe(res => {
          if (res.body! === true) {
            swalSuccessWithMessage('Successfully Changed RRF status from CLOSED to OPEN!');
            this.deletingData = false;
          } else {
            swalForWarningWithMessage('Request Failed!');
            this.deletingData = false;
          }
        });
      }
    });
  }

  changeFlag(): void {
    if (this.isLeaveApplicationEnabled) this.leaveApplicationEnableConfig.value = 'FALSE';
    else this.leaveApplicationEnableConfig.value = 'TRUE';
    this.configService.update(this.leaveApplicationEnableConfig).subscribe(res => {
      this.leaveApplicationEnableConfig = res.body!;
      this.isLeaveApplicationEnabled = !this.isLeaveApplicationEnabled;
      if (this.isLeaveApplicationEnabled) swalSuccessWithMessage('Enabled');
      else swalForWarningWithMessage('Disabled');
    });
  }

  runScheduledInsuranceService(): void {
    const warningMessage = 'Scheduled Service Will Do: Exclude Resigned Employees, Exclude Child if Exceeds Max Age Limit.';
    swalConfirmationWithMessage(warningMessage).then(result => {
      if (result.isConfirmed) {
        this.importDataService.runScheduledInsuranceService().subscribe(
          res => {
            if (res.body!) {
              swalOnAppliedSuccess();
            }
          },
          err => {
            swalOnRequestError();
          }
        );
      }
    });
  }

  resetInsuranceBalance(): void {
    const warningMessage = 'Reset all insurance balance?';
    swalConfirmationWithMessage(warningMessage).then(result => {
      if (result.isConfirmed) {
        this.importDataService.resetInsuranceBalance().subscribe(
          res => {
            if (res.body!) {
              swalOnAppliedSuccess();
            }
          },
          err => {
            swalOnRequestError();
          }
        );
      }
    });
  }

  runEmployeePinScheduler(): void {
    const warningMessage = 'Are You Sure?';
    swalOnBatchDeleteConfirmation(warningMessage).then(result => {
      if (result.isConfirmed) {
        this.importDataService.runEmployeePinScheduler().subscribe(res => {
          swalSuccessWithMessage('Successful!');
        });
      }
    });
  }

  trimPIN(type: string): void {
    swalOnLoading('PIN trimming on process');
    this.importDataService.trimEmployeePin(type).subscribe(
      _ => {
        if (_.body) {
          swalOnUpdatedSuccess();
        }
      },
      () => swalForErrorWithMessage('Failed!')
    );
  }

  fixMultipleJoiningHistory(): void {
    swalOnLoading('Joining history fixing on process');
    this.importDataService.fixMultipleJoiningHistory().subscribe(
      _ => {
        if (_.body) {
          swalSuccessWithMessage('Fixed!');
        }
      },
      () => swalForErrorWithMessage('Failed!')
    );
  }
}
