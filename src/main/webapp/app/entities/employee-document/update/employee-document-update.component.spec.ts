import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeeDocumentFormService } from './employee-document-form.service';
import { EmployeeDocumentService } from '../service/employee-document.service';
import { IEmployeeDocument } from '../employee-document.model';

import { EmployeeDocumentUpdateComponent } from './employee-document-update.component';

describe('EmployeeDocument Management Update Component', () => {
  let comp: EmployeeDocumentUpdateComponent;
  let fixture: ComponentFixture<EmployeeDocumentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeDocumentFormService: EmployeeDocumentFormService;
  let employeeDocumentService: EmployeeDocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeeDocumentUpdateComponent],
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
      .overrideTemplate(EmployeeDocumentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeDocumentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeDocumentFormService = TestBed.inject(EmployeeDocumentFormService);
    employeeDocumentService = TestBed.inject(EmployeeDocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const employeeDocument: IEmployeeDocument = { id: 456 };

      activatedRoute.data = of({ employeeDocument });
      comp.ngOnInit();

      expect(comp.employeeDocument).toEqual(employeeDocument);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeDocument>>();
      const employeeDocument = { id: 123 };
      jest.spyOn(employeeDocumentFormService, 'getEmployeeDocument').mockReturnValue(employeeDocument);
      jest.spyOn(employeeDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeDocument }));
      saveSubject.complete();

      // THEN
      expect(employeeDocumentFormService.getEmployeeDocument).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeDocumentService.update).toHaveBeenCalledWith(expect.objectContaining(employeeDocument));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeDocument>>();
      const employeeDocument = { id: 123 };
      jest.spyOn(employeeDocumentFormService, 'getEmployeeDocument').mockReturnValue({ id: null });
      jest.spyOn(employeeDocumentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeDocument: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeDocument }));
      saveSubject.complete();

      // THEN
      expect(employeeDocumentFormService.getEmployeeDocument).toHaveBeenCalled();
      expect(employeeDocumentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeDocument>>();
      const employeeDocument = { id: 123 };
      jest.spyOn(employeeDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeDocumentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
