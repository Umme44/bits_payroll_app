import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmployeeDocumentDetailComponent } from './employee-document-detail.component';

describe('EmployeeDocument Management Detail Component', () => {
  let comp: EmployeeDocumentDetailComponent;
  let fixture: ComponentFixture<EmployeeDocumentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeeDocumentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ employeeDocument: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EmployeeDocumentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmployeeDocumentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load employeeDocument on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.employeeDocument).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
