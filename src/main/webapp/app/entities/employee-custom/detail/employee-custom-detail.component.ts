import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';

import { IAllowanceName } from 'app/shared/model/allowance-name.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IEmployee } from '../employee-custom.model';
import { IEducationDetails } from '../../education-details/education-details.model';
import { IWorkingExperience } from '../../working-experience/working-experience.model';
import { ITrainingHistory } from '../../training-history/training-history.model';
import { IReferences } from '../../references/references.model';
import { ConfigService } from '../../../shared/legacy/legacy-service/config.service';
import { EducationDetailsService } from '../../education-details/service/education-details.service';
import { WorkingExperienceService } from '../../working-experience/service/working-experience.service';
import { TrainingHistoryService } from '../../training-history/service/training-history.service';
import { ReferencesService } from '../../references/service/references.service';
import { EducationDetailsModalComponent } from '../modals/employee-eduction-details/education-details.modal.component';
import { EmployeeWorkExperienceModalComponent } from '../modals/employee-work-experiences/employee-work-experience.modal.component';
import { EmployeeTrainingHistoryModalComponent } from '../modals/employee-training-history/employee-training-history.modal.component';
import { EmployeeReferencesModalComponent } from '../modals/employee-references/employee-references.modal.component';

@Component({
  selector: 'jhi-employee-custom-detail',
  templateUrl: './employee-custom-detail.component.html',
  styleUrls: ['../list/employee-custom.component.scss'],
})
export class EmployeeCustomDetailComponent implements OnInit {
  employee: IEmployee | null = null;
  active = 'top';
  selectedNav = 'basic';
  nextId = '';

  allowanceName!: IAllowanceName;
  educationDetails!: IEducationDetails[];
  workingExperiences!: IWorkingExperience[];
  trainingHistories!: ITrainingHistory[];
  references!: IReferences[];

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected configService: ConfigService,
    private modalService: NgbModal,
    protected educationDetailsService: EducationDetailsService,
    protected workingExperienceService: WorkingExperienceService,
    protected trainingHistoryService: TrainingHistoryService,
    protected referencesService: ReferencesService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => (this.employee = employee));
    this.configService
      .getAllowanceName()
      .subscribe((allowanceName: HttpResponse<IAllowanceName>) => (this.allowanceName = allowanceName.body!));
  }

  previousState(): void {
    window.history.back();
  }

  loadEducationDetails(): void {
    this.educationDetailsService
      .queryByEmployeeId(this.employee?.id!)
      .subscribe((res: HttpResponse<IEducationDetails[]>) => (this.educationDetails = res.body || []));
  }

  loadWorkExperiences(): void {
    this.workingExperienceService
      .queryByEmployeeId(this.employee?.id!)
      .subscribe((res: HttpResponse<IWorkingExperience[]>) => (this.workingExperiences = res.body || []));
  }

  loadTrainingHistories(): void {
    this.trainingHistoryService
      .queryByEmployeeId(this.employee?.id!)
      .subscribe((res: HttpResponse<ITrainingHistory[]>) => (this.trainingHistories = res.body || []));
  }

  loadReferences(): void {
    this.referencesService
      .queryByEmployeeId(this.employee?.id!)
      .subscribe((res: HttpResponse<IReferences[]>) => (this.references = res.body || []));
  }

  openEducationDetails(event: any): void {
    const modalRef = this.modalService.open(EducationDetailsModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.employeeId = this.employee?.id;
    modalRef.componentInstance.employee = this.employee;

    modalRef.result.then(
      result => {},
      reason => {
        this.loadEducationDetails();
      }
    );
  }

  openWorkExperiences(event: any): void {
    const modalRef = this.modalService.open(EmployeeWorkExperienceModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.employeeId = this.employee?.id;
    modalRef.componentInstance.employee = this.employee;

    modalRef.result.then(
      result => {},
      reason => {
        this.loadWorkExperiences();
      }
    );
  }

  openTrainingHistories(event: any): void {
    const modalRef = this.modalService.open(EmployeeTrainingHistoryModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.employeeId = this.employee?.id;
    modalRef.componentInstance.employee = this.employee;

    modalRef.result.then(
      result => {},
      reason => {
        this.loadTrainingHistories();
      }
    );
  }

  openReferences(event: any): void {
    if (this.employee && this.employee.id) {
      const modalRef = this.modalService.open(EmployeeReferencesModalComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.employeeId = this.employee.id;
      modalRef.componentInstance.employee = this.employee;

      modalRef.result.then(
        result => {},
        reason => {
          this.loadReferences();
        }
      );
    }
  }

  trackId(index: number, item: IEducationDetails): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  tabChange($event: any): void {
    this.nextId = $event.nextId;
    if (this.nextId === 'educationDetails') {
      this.loadEducationDetails();
    } else if (this.nextId === 'workExperience') {
      this.loadWorkExperiences();
    } else if (this.nextId === 'trainingHistories') {
      this.loadTrainingHistories();
    } else if (this.nextId === 'references') {
      this.loadReferences();
    }
  }

  navChange(event: any) {
    if (this.selectedNav === 'educationDetails') {
      this.loadEducationDetails();
    } else if (this.selectedNav === 'workExperience') {
      this.loadWorkExperiences();
    } else if (this.selectedNav === 'trainingHistories') {
      this.loadTrainingHistories();
    } else if (this.selectedNav === 'references') {
      this.loadReferences();
    }
  }
}
