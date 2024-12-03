import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { NomineeFormService } from './nominee-form.service';
import { NomineeService } from '../service/nominee.service';
import { INominee } from '../nominee.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { NomineeUpdateComponent } from './nominee-update.component';

describe('Nominee Management Update Component', () => {
  let comp: NomineeUpdateComponent;
  let fixture: ComponentFixture<NomineeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let nomineeFormService: NomineeFormService;
  let nomineeService: NomineeService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [NomineeUpdateComponent],
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
      .overrideTemplate(NomineeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NomineeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    nomineeFormService = TestBed.inject(NomineeFormService);
    nomineeService = TestBed.inject(NomineeService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const nominee: INominee = { id: 456 };
      const employee: IEmployee = { id: 29857 };
      nominee.employee = employee;
      const approvedBy: IEmployee = { id: 85543 };
      nominee.approvedBy = approvedBy;
      const witness: IEmployee = { id: 38245 };
      nominee.witness = witness;
      const member: IEmployee = { id: 95213 };
      nominee.member = member;

      const employeeCollection: IEmployee[] = [{ id: 18792 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee, approvedBy, witness, member];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ nominee });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const nominee: INominee = { id: 456 };
      const employee: IEmployee = { id: 58256 };
      nominee.employee = employee;
      const approvedBy: IEmployee = { id: 73336 };
      nominee.approvedBy = approvedBy;
      const witness: IEmployee = { id: 68418 };
      nominee.witness = witness;
      const member: IEmployee = { id: 32249 };
      nominee.member = member;

      activatedRoute.data = of({ nominee });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.employeesSharedCollection).toContain(approvedBy);
      expect(comp.employeesSharedCollection).toContain(witness);
      expect(comp.employeesSharedCollection).toContain(member);
      expect(comp.nominee).toEqual(nominee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INominee>>();
      const nominee = { id: 123 };
      jest.spyOn(nomineeFormService, 'getNominee').mockReturnValue(nominee);
      jest.spyOn(nomineeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nominee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nominee }));
      saveSubject.complete();

      // THEN
      expect(nomineeFormService.getNominee).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(nomineeService.update).toHaveBeenCalledWith(expect.objectContaining(nominee));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INominee>>();
      const nominee = { id: 123 };
      jest.spyOn(nomineeFormService, 'getNominee').mockReturnValue({ id: null });
      jest.spyOn(nomineeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nominee: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nominee }));
      saveSubject.complete();

      // THEN
      expect(nomineeFormService.getNominee).toHaveBeenCalled();
      expect(nomineeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INominee>>();
      const nominee = { id: 123 };
      jest.spyOn(nomineeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nominee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(nomineeService.update).toHaveBeenCalled();
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
  });
});
