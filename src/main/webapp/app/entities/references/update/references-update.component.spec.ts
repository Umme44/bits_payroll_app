import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ReferencesFormService } from './references-form.service';
import { ReferencesService } from '../service/references.service';
import { IReferences } from '../references.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { ReferencesUpdateComponent } from './references-update.component';

describe('References Management Update Component', () => {
  let comp: ReferencesUpdateComponent;
  let fixture: ComponentFixture<ReferencesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let referencesFormService: ReferencesFormService;
  let referencesService: ReferencesService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ReferencesUpdateComponent],
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
      .overrideTemplate(ReferencesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReferencesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    referencesFormService = TestBed.inject(ReferencesFormService);
    referencesService = TestBed.inject(ReferencesService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const references: IReferences = { id: 456 };
      const employee: IEmployee = { id: 52072 };
      references.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 54057 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ references });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const references: IReferences = { id: 456 };
      const employee: IEmployee = { id: 88 };
      references.employee = employee;

      activatedRoute.data = of({ references });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.references).toEqual(references);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReferences>>();
      const references = { id: 123 };
      jest.spyOn(referencesFormService, 'getReferences').mockReturnValue(references);
      jest.spyOn(referencesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ references });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: references }));
      saveSubject.complete();

      // THEN
      expect(referencesFormService.getReferences).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(referencesService.update).toHaveBeenCalledWith(expect.objectContaining(references));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReferences>>();
      const references = { id: 123 };
      jest.spyOn(referencesFormService, 'getReferences').mockReturnValue({ id: null });
      jest.spyOn(referencesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ references: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: references }));
      saveSubject.complete();

      // THEN
      expect(referencesFormService.getReferences).toHaveBeenCalled();
      expect(referencesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReferences>>();
      const references = { id: 123 };
      jest.spyOn(referencesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ references });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(referencesService.update).toHaveBeenCalled();
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
