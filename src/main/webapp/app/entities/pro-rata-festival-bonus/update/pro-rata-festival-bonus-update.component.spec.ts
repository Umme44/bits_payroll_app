import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProRataFestivalBonusFormService } from './pro-rata-festival-bonus-form.service';
import { ProRataFestivalBonusService } from '../service/pro-rata-festival-bonus.service';
import { IProRataFestivalBonus } from '../pro-rata-festival-bonus.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { ProRataFestivalBonusUpdateComponent } from './pro-rata-festival-bonus-update.component';

describe('ProRataFestivalBonus Management Update Component', () => {
  let comp: ProRataFestivalBonusUpdateComponent;
  let fixture: ComponentFixture<ProRataFestivalBonusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let proRataFestivalBonusFormService: ProRataFestivalBonusFormService;
  let proRataFestivalBonusService: ProRataFestivalBonusService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProRataFestivalBonusUpdateComponent],
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
      .overrideTemplate(ProRataFestivalBonusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProRataFestivalBonusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    proRataFestivalBonusFormService = TestBed.inject(ProRataFestivalBonusFormService);
    proRataFestivalBonusService = TestBed.inject(ProRataFestivalBonusService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const proRataFestivalBonus: IProRataFestivalBonus = { id: 456 };
      const employee: IEmployee = { id: 95979 };
      proRataFestivalBonus.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 24663 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ proRataFestivalBonus });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const proRataFestivalBonus: IProRataFestivalBonus = { id: 456 };
      const employee: IEmployee = { id: 39492 };
      proRataFestivalBonus.employee = employee;

      activatedRoute.data = of({ proRataFestivalBonus });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.proRataFestivalBonus).toEqual(proRataFestivalBonus);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProRataFestivalBonus>>();
      const proRataFestivalBonus = { id: 123 };
      jest.spyOn(proRataFestivalBonusFormService, 'getProRataFestivalBonus').mockReturnValue(proRataFestivalBonus);
      jest.spyOn(proRataFestivalBonusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proRataFestivalBonus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: proRataFestivalBonus }));
      saveSubject.complete();

      // THEN
      expect(proRataFestivalBonusFormService.getProRataFestivalBonus).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(proRataFestivalBonusService.update).toHaveBeenCalledWith(expect.objectContaining(proRataFestivalBonus));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProRataFestivalBonus>>();
      const proRataFestivalBonus = { id: 123 };
      jest.spyOn(proRataFestivalBonusFormService, 'getProRataFestivalBonus').mockReturnValue({ id: null });
      jest.spyOn(proRataFestivalBonusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proRataFestivalBonus: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: proRataFestivalBonus }));
      saveSubject.complete();

      // THEN
      expect(proRataFestivalBonusFormService.getProRataFestivalBonus).toHaveBeenCalled();
      expect(proRataFestivalBonusService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProRataFestivalBonus>>();
      const proRataFestivalBonus = { id: 123 };
      jest.spyOn(proRataFestivalBonusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proRataFestivalBonus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(proRataFestivalBonusService.update).toHaveBeenCalled();
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
