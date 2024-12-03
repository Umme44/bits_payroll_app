import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeeStaticFileFormService } from './employee-static-file-form.service';
import { EmployeeStaticFileService } from '../service/employee-static-file.service';
import { IEmployeeStaticFile } from '../employee-static-file.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { EmployeeStaticFileUpdateComponent } from './employee-static-file-update.component';

describe('EmployeeStaticFile Management Update Component', () => {
  let comp: EmployeeStaticFileUpdateComponent;
  let fixture: ComponentFixture<EmployeeStaticFileUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeStaticFileFormService: EmployeeStaticFileFormService;
  let employeeStaticFileService: EmployeeStaticFileService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeeStaticFileUpdateComponent],
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
      .overrideTemplate(EmployeeStaticFileUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeStaticFileUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeStaticFileFormService = TestBed.inject(EmployeeStaticFileFormService);
    employeeStaticFileService = TestBed.inject(EmployeeStaticFileService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const employeeStaticFile: IEmployeeStaticFile = { id: 456 };
      const employee: IEmployee = { id: 32083 };
      employeeStaticFile.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 73634 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeeStaticFile });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employeeStaticFile: IEmployeeStaticFile = { id: 456 };
      const employee: IEmployee = { id: 56637 };
      employeeStaticFile.employee = employee;

      activatedRoute.data = of({ employeeStaticFile });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.employeeStaticFile).toEqual(employeeStaticFile);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeStaticFile>>();
      const employeeStaticFile = { id: 123 };
      jest.spyOn(employeeStaticFileFormService, 'getEmployeeStaticFile').mockReturnValue(employeeStaticFile);
      jest.spyOn(employeeStaticFileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeStaticFile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeStaticFile }));
      saveSubject.complete();

      // THEN
      expect(employeeStaticFileFormService.getEmployeeStaticFile).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeStaticFileService.update).toHaveBeenCalledWith(expect.objectContaining(employeeStaticFile));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeStaticFile>>();
      const employeeStaticFile = { id: 123 };
      jest.spyOn(employeeStaticFileFormService, 'getEmployeeStaticFile').mockReturnValue({ id: null });
      jest.spyOn(employeeStaticFileService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeStaticFile: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeStaticFile }));
      saveSubject.complete();

      // THEN
      expect(employeeStaticFileFormService.getEmployeeStaticFile).toHaveBeenCalled();
      expect(employeeStaticFileService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeStaticFile>>();
      const employeeStaticFile = { id: 123 };
      jest.spyOn(employeeStaticFileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeStaticFile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeStaticFileService.update).toHaveBeenCalled();
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
