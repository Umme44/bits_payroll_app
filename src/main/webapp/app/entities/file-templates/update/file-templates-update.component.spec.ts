import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FileTemplatesFormService } from './file-templates-form.service';
import { FileTemplatesService } from '../service/file-templates.service';
import { IFileTemplates } from '../file-templates.model';

import { FileTemplatesUpdateComponent } from './file-templates-update.component';

describe('FileTemplates Management Update Component', () => {
  let comp: FileTemplatesUpdateComponent;
  let fixture: ComponentFixture<FileTemplatesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fileTemplatesFormService: FileTemplatesFormService;
  let fileTemplatesService: FileTemplatesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FileTemplatesUpdateComponent],
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
      .overrideTemplate(FileTemplatesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FileTemplatesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fileTemplatesFormService = TestBed.inject(FileTemplatesFormService);
    fileTemplatesService = TestBed.inject(FileTemplatesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fileTemplates: IFileTemplates = { id: 456 };

      activatedRoute.data = of({ fileTemplates });
      comp.ngOnInit();

      expect(comp.fileTemplates).toEqual(fileTemplates);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFileTemplates>>();
      const fileTemplates = { id: 123 };
      jest.spyOn(fileTemplatesFormService, 'getFileTemplates').mockReturnValue(fileTemplates);
      jest.spyOn(fileTemplatesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileTemplates });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fileTemplates }));
      saveSubject.complete();

      // THEN
      expect(fileTemplatesFormService.getFileTemplates).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fileTemplatesService.update).toHaveBeenCalledWith(expect.objectContaining(fileTemplates));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFileTemplates>>();
      const fileTemplates = { id: 123 };
      jest.spyOn(fileTemplatesFormService, 'getFileTemplates').mockReturnValue({ id: null });
      jest.spyOn(fileTemplatesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileTemplates: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fileTemplates }));
      saveSubject.complete();

      // THEN
      expect(fileTemplatesFormService.getFileTemplates).toHaveBeenCalled();
      expect(fileTemplatesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFileTemplates>>();
      const fileTemplates = { id: 123 };
      jest.spyOn(fileTemplatesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileTemplates });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fileTemplatesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
