import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PfArrearFormService } from './pf-arrear-form.service';
import { PfArrearService } from '../service/pf-arrear.service';
import { IPfArrear } from '../pf-arrear.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { PfArrearUpdateComponent } from './pf-arrear-update.component';

describe('PfArrear Management Update Component', () => {
  let comp: PfArrearUpdateComponent;
  let fixture: ComponentFixture<PfArrearUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pfArrearFormService: PfArrearFormService;
  let pfArrearService: PfArrearService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PfArrearUpdateComponent],
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
      .overrideTemplate(PfArrearUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PfArrearUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pfArrearFormService = TestBed.inject(PfArrearFormService);
    pfArrearService = TestBed.inject(PfArrearService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const pfArrear: IPfArrear = { id: 456 };
      const employee: IEmployee = { id: 27263 };
      pfArrear.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 49875 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pfArrear });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pfArrear: IPfArrear = { id: 456 };
      const employee: IEmployee = { id: 49417 };
      pfArrear.employee = employee;

      activatedRoute.data = of({ pfArrear });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.pfArrear).toEqual(pfArrear);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfArrear>>();
      const pfArrear = { id: 123 };
      jest.spyOn(pfArrearFormService, 'getPfArrear').mockReturnValue(pfArrear);
      jest.spyOn(pfArrearService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfArrear });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pfArrear }));
      saveSubject.complete();

      // THEN
      expect(pfArrearFormService.getPfArrear).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pfArrearService.update).toHaveBeenCalledWith(expect.objectContaining(pfArrear));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfArrear>>();
      const pfArrear = { id: 123 };
      jest.spyOn(pfArrearFormService, 'getPfArrear').mockReturnValue({ id: null });
      jest.spyOn(pfArrearService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfArrear: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pfArrear }));
      saveSubject.complete();

      // THEN
      expect(pfArrearFormService.getPfArrear).toHaveBeenCalled();
      expect(pfArrearService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfArrear>>();
      const pfArrear = { id: 123 };
      jest.spyOn(pfArrearService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfArrear });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pfArrearService.update).toHaveBeenCalled();
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
