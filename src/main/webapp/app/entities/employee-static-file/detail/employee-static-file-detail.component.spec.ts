import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmployeeStaticFileDetailComponent } from './employee-static-file-detail.component';

describe('EmployeeStaticFile Management Detail Component', () => {
  let comp: EmployeeStaticFileDetailComponent;
  let fixture: ComponentFixture<EmployeeStaticFileDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeeStaticFileDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ employeeStaticFile: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EmployeeStaticFileDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmployeeStaticFileDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load employeeStaticFile on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.employeeStaticFile).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
