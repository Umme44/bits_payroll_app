import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FestivalBonusDetailsFormService } from './festival-bonus-details-form.service';
import { FestivalBonusDetailsService } from '../service/festival-bonus-details.service';
import { IFestivalBonusDetails } from '../festival-bonus-details.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IFestival } from 'app/entities/festival/festival.model';
import { FestivalService } from 'app/entities/festival/service/festival.service';

import { FestivalBonusDetailsUpdateComponent } from './festival-bonus-details-update.component';

describe('FestivalBonusDetails Management Update Component', () => {
  let comp: FestivalBonusDetailsUpdateComponent;
  let fixture: ComponentFixture<FestivalBonusDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let festivalBonusDetailsFormService: FestivalBonusDetailsFormService;
  let festivalBonusDetailsService: FestivalBonusDetailsService;
  let employeeService: EmployeeService;
  let festivalService: FestivalService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FestivalBonusDetailsUpdateComponent],
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
      .overrideTemplate(FestivalBonusDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FestivalBonusDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    festivalBonusDetailsFormService = TestBed.inject(FestivalBonusDetailsFormService);
    festivalBonusDetailsService = TestBed.inject(FestivalBonusDetailsService);
    employeeService = TestBed.inject(EmployeeService);
    festivalService = TestBed.inject(FestivalService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const festivalBonusDetails: IFestivalBonusDetails = { id: 456 };
      const employee: IEmployee = { id: 24351 };
      festivalBonusDetails.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 80598 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ festivalBonusDetails });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Festival query and add missing value', () => {
      const festivalBonusDetails: IFestivalBonusDetails = { id: 456 };
      const festival: IFestival = { id: 93871 };
      festivalBonusDetails.festival = festival;

      const festivalCollection: IFestival[] = [{ id: 36408 }];
      jest.spyOn(festivalService, 'query').mockReturnValue(of(new HttpResponse({ body: festivalCollection })));
      const additionalFestivals = [festival];
      const expectedCollection: IFestival[] = [...additionalFestivals, ...festivalCollection];
      jest.spyOn(festivalService, 'addFestivalToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ festivalBonusDetails });
      comp.ngOnInit();

      expect(festivalService.query).toHaveBeenCalled();
      expect(festivalService.addFestivalToCollectionIfMissing).toHaveBeenCalledWith(
        festivalCollection,
        ...additionalFestivals.map(expect.objectContaining)
      );
      expect(comp.festivalsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const festivalBonusDetails: IFestivalBonusDetails = { id: 456 };
      const employee: IEmployee = { id: 42251 };
      festivalBonusDetails.employee = employee;
      const festival: IFestival = { id: 62093 };
      festivalBonusDetails.festival = festival;

      activatedRoute.data = of({ festivalBonusDetails });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.festivalsSharedCollection).toContain(festival);
      expect(comp.festivalBonusDetails).toEqual(festivalBonusDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFestivalBonusDetails>>();
      const festivalBonusDetails = { id: 123 };
      jest.spyOn(festivalBonusDetailsFormService, 'getFestivalBonusDetails').mockReturnValue(festivalBonusDetails);
      jest.spyOn(festivalBonusDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ festivalBonusDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: festivalBonusDetails }));
      saveSubject.complete();

      // THEN
      expect(festivalBonusDetailsFormService.getFestivalBonusDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(festivalBonusDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(festivalBonusDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFestivalBonusDetails>>();
      const festivalBonusDetails = { id: 123 };
      jest.spyOn(festivalBonusDetailsFormService, 'getFestivalBonusDetails').mockReturnValue({ id: null });
      jest.spyOn(festivalBonusDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ festivalBonusDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: festivalBonusDetails }));
      saveSubject.complete();

      // THEN
      expect(festivalBonusDetailsFormService.getFestivalBonusDetails).toHaveBeenCalled();
      expect(festivalBonusDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFestivalBonusDetails>>();
      const festivalBonusDetails = { id: 123 };
      jest.spyOn(festivalBonusDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ festivalBonusDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(festivalBonusDetailsService.update).toHaveBeenCalled();
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

    describe('compareFestival', () => {
      it('Should forward to festivalService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(festivalService, 'compareFestival');
        comp.compareFestival(entity, entity2);
        expect(festivalService.compareFestival).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
