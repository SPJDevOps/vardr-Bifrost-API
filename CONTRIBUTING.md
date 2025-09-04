# Contributing to Bifrost-API

Thank you for your interest in contributing to Bifrost-API! This document provides guidelines and information for contributors.

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Docker and Docker Compose
- Git
- Your favorite IDE (IntelliJ IDEA recommended for Kotlin)

### Development Setup
1. Fork the repository
2. Clone your fork: `git clone https://github.com/your-username/bifrost-api.git`
3. Start infrastructure services: `docker-compose up -d`
4. Run the application: `./gradlew bootRun`
5. Run tests: `./gradlew test`

## 📝 Development Guidelines

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add documentation for public APIs
- Keep functions focused and small

### Testing
- Write unit tests for new functionality
- Ensure all tests pass before submitting PR
- Include integration tests for API endpoints
- Test authentication and authorization scenarios

### Git Workflow
1. Create a feature branch: `git checkout -b feature/description`
2. Make your changes
3. Write or update tests
4. Ensure all tests pass
5. Update documentation if needed
6. Commit with descriptive messages
7. Push to your fork
8. Create a Pull Request

### Commit Messages
Use conventional commit format:
- `feat: add new scheduling type`
- `fix: resolve authentication issue`
- `docs: update API documentation`
- `test: add integration tests`
- `refactor: improve service layer`

## 🏗️ Architecture Guidelines

### Package Structure
- `downloads/` - Core download functionality
- `scheduling/` - Scheduling system
- `security/` - Authentication and authorization
- `queues/` - Message queue integration
- `exceptions/` - Custom exceptions

### Database Changes
- Use Flyway migrations for schema changes
- Include both up and down migrations
- Test migrations on clean database
- Document breaking changes

### API Design
- Follow RESTful principles
- Use appropriate HTTP methods
- Include proper error handling
- Version APIs when making breaking changes
- Document all endpoints

## 🔧 Common Tasks

### Adding New Endpoints
1. Create controller with proper annotations
2. Add service layer logic
3. Include input validation
4. Add authentication/authorization
5. Write comprehensive tests
6. Update API documentation

### Adding New Schedule Types
1. Update `ScheduleType` enum
2. Implement calculation logic in `ScheduleService`
3. Add validation for new parameters
4. Update frontend integration
5. Add tests for new schedule type

### Database Schema Changes
1. Create Flyway migration script
2. Update entity classes
3. Update repository methods if needed
4. Test migration thoroughly
5. Update documentation

## 🧪 Testing

### Running Tests
```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "DownloadServiceTest"

# Run with coverage
./gradlew test jacocoTestReport
```

### Test Categories
- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test API endpoints with real dependencies
- **Security Tests**: Test authentication and authorization
- **Database Tests**: Test repository layer with test database

## 📚 Documentation

### Code Documentation
- Add KDoc comments for public APIs
- Include parameter descriptions
- Document complex business logic
- Add examples where helpful

### API Documentation
- Update OpenAPI/Swagger documentation
- Include request/response examples
- Document authentication requirements
- Explain error responses

## 🐛 Bug Reports

When reporting bugs, include:
- Clear description of the issue
- Steps to reproduce
- Expected vs actual behavior
- Environment details (OS, Java version, etc.)
- Relevant log files or error messages

## 💡 Feature Requests

For new features, provide:
- Clear use case description
- Proposed solution approach
- Alternative solutions considered
- Impact on existing functionality
- Any breaking changes

## 🔍 Code Review Process

### Before Submitting PR
- [ ] All tests pass
- [ ] Code follows style guidelines
- [ ] Documentation is updated
- [ ] No security vulnerabilities introduced
- [ ] Performance impact considered

### Review Criteria
- Code quality and maintainability
- Test coverage and quality
- Documentation completeness
- Security considerations
- Performance implications
- Backward compatibility

## 📞 Getting Help

- **GitHub Issues**: For bug reports and feature requests
- **GitHub Discussions**: For questions and general discussion
- **Documentation**: Check the README and wiki first

## 🙏 Recognition

Contributors will be recognized in:
- CONTRIBUTORS.md file
- Release notes for significant contributions
- GitHub contributor statistics

Thank you for contributing to Bifrost-API! 🌈
