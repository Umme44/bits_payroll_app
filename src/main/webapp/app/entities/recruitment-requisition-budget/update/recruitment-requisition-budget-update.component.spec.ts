import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RecruitmentRequisitionBudgetFormService } from './recruitment-requisition-budget-form.service';
import { RecruitmentRequisitionBudgetService } from '../service/recruitment-requisition-budget.service';
import { IRecruitmentRequisitionBudget } from '../recruitment-requisition-budget.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';

import { RecruitmentRequisitionBudgetUpdateComponent } from './recruitment-requisition-budget-update.component';

describe('RecruitmentRequisitionBudget Management Update Component', () => {
  let comp: RecruitmentRequisitionBudgetUpdateComponent;
  let fixture: ComponentFixture<RecruitmentRequisitionBudgetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let recruitmentRequisitionBudgetFormService: RecruitmentRequisitionBudgetFormService;
  let recruitmentRequisitionBudgetService: RecruitmentRequisitionBudgetService;
  let employeeService: EmployeeService;
  let departmentService: DepartmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RecruitmentRequisitionBudgetUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(RecruitmentRequisitionBudgetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RecruitmentRequisitionBudgetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    recruitmentRequisitionBudgetFormService = TestBed.inject(RecruitmentRequisitionBudgetFormService);
    recruitmentRequisitionBudgetService = TestBed.inject(RecruitmentRequisitionBudgetService);
    employeeService = TestBed.inject(EmployeeService);
    departmentService = TestBed.inject(DepartmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const recruitmentRequisitionBudget: IRecruitmentRequisitionBudget = { id: 456 };
      const employee: IEmployee = { id: 65293 };
      recruitmentRequisitionBudget.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 88672 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recruitmentRequisitionBudget });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Department query and add missing value', () => {
      const recruitmentRequisitionBudget: IRecruitmentRequisitionBudget = { id: 456 };
      const department: IDepartment = { id: 69882 };
      recruitmentRequisitionBudget.department = department;

      const departmentCollection: IDepartment[] = [{ id: 97112 }];
      jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [department];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recruitmentRequisitionBudget });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(
        departmentCollection,
        ...additionalDepartments.map(expect.objectContaining)
      );
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const recruitmentRequisitionBudget: IRecruitmentRequisitionBudget = { id: 456 };
      const employee: IEmployee = { id: 12193 };
      recruitmentRequisitionBudget.employee = employee;
      const department: IDepartment = { id: 52979 };
      recruitmentRequisitionBudget.department = department;

      activatedRoute.data = of({ recruitmentRequisitionBudget });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.departmentsSharedCollection).toContain(department);
      expect(comp.recruitmentRequisitionBudget).toEqual(recruitmentRequisitionBudget);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecruitmentRequisitionBudget>>();
      const recruitmentRequisitionBudget = { id: 123 };
      jest.spyOn(recruitmentRequisitionBudgetFormService, 'getRecruitmentRequisitionBudget').mockReturnValue(recruitmentRequisitionBudget);
      jest.spyOn(recruitmentRequisitionBudgetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recruitmentRequisitionBudget });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recruitmentRequisitionBudget }));
      saveSubject.complete();

      // THEN
      expect(recruitmentRequisitionBudgetFormService.getRecruitmentRequisitionBudget).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(recruitmentRequisitionBudgetService.update).toHaveBeenCalledWith(expect.objectContaining(recruitmentRequisitionBudget));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecruitmentRequisitionBudget>>();
      const recruitmentRequisitionBudget = { id: 123 };
      jest.spyOn(recruitmentRequisitionBudgetFormService, 'getRecruitmentRequisitionBudget').mockReturnValue({ id: null });
      jest.spyOn(recruitmentRequisitionBudgetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recruitmentRequisitionBudget: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recruitmentRequisitionBudget }));
      saveSubject.complete();

      // THEN
      expect(recruitmentRequisitionBudgetFormService.getRecruitmentRequisitionBudget).toHaveBeenCalled();
      expect(recruitmentRequisitionBudgetService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecruitmentRequisitionBudget>>();
      const recruitmentRequisitionBudget = { id: 123 };
      jest.spyOn(recruitmentRequisitionBudgetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recruitmentRequisitionBudget });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(recruitmentRequisitionBudgetService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDepartment', () => {
      it('Should forward to departmentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departmentService, 'compareDepartment');
        comp.compareDepartment(entity, entity2);
        expect(departmentService.compareDepartment).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
