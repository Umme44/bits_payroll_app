import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MobileBillFormService } from './mobile-bill-form.service';
import { MobileBillService } from '../service/mobile-bill.service';
import { IMobileBill } from '../mobile-bill.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { MobileBillUpdateComponent } from './mobile-bill-update.component';

describe('MobileBill Management Update Component', () => {
  let comp: MobileBillUpdateComponent;
  let fixture: ComponentFixture<MobileBillUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let mobileBillFormService: MobileBillFormService;
  let mobileBillService: MobileBillService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MobileBillUpdateComponent],
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
      .overrideTemplate(MobileBillUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MobileBillUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mobileBillFormService = TestBed.inject(MobileBillFormService);
    mobileBillService = TestBed.inject(MobileBillService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const mobileBill: IMobileBill = { id: 456 };
      const employee: IEmployee = { id: 93648 };
      mobileBill.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 95390 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ mobileBill });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const mobileBill: IMobileBill = { id: 456 };
      const employee: IEmployee = { id: 83865 };
      mobileBill.employee = employee;

      activatedRoute.data = of({ mobileBill });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.mobileBill).toEqual(mobileBill);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMobileBill>>();
      const mobileBill = { id: 123 };
      jest.spyOn(mobileBillFormService, 'getMobileBill').mockReturnValue(mobileBill);
      jest.spyOn(mobileBillService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mobileBill });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mobileBill }));
      saveSubject.complete();

      // THEN
      expect(mobileBillFormService.getMobileBill).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(mobileBillService.update).toHaveBeenCalledWith(expect.objectContaining(mobileBill));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMobileBill>>();
      const mobileBill = { id: 123 };
      jest.spyOn(mobileBillFormService, 'getMobileBill').mockReturnValue({ id: null });
      jest.spyOn(mobileBillService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mobileBill: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mobileBill }));
      saveSubject.complete();

      // THEN
      expect(mobileBillFormService.getMobileBill).toHaveBeenCalled();
      expect(mobileBillService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMobileBill>>();
      const mobileBill = { id: 123 };
      jest.spyOn(mobileBillService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mobileBill });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mobileBillService.update).toHaveBeenCalled();
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
